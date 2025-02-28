import hashlib
import json
from time import time
from urllib.parse import urlparse
from uuid import uuid4
import random

import requests
from flask import Flask, jsonify, request


class Blockchain:
    def __init__(self):
        self.current_transactions = []
        self.chain = []
        self.nodes = set()
        # Initialize stakes for validators (in a real system, this would be more sophisticated)
        self.stakes = {
            'node1': 100,  # Example stake for node1
            'node2': 150,  # Example stake for node2
            'node3': 200,  # Example stake for node3
        }
        self.validators = list(self.stakes.keys())
        
        # Create the genesis block
        self.new_block(previous_hash='1', validator='genesis')

    def register_node(self, address):
        """
        Add a new node to the list of nodes

        :param address: Address of node. Eg. 'http://192.168.0.5:5000'
        """

        parsed_url = urlparse(address)
        if parsed_url.netloc:
            self.nodes.add(parsed_url.netloc)
        elif parsed_url.path:
            # Accepts an URL without scheme like '192.168.0.5:5000'.
            self.nodes.add(parsed_url.path)
        else:
            raise ValueError('Invalid URL')

    def valid_chain(self, chain):
        """
        Determine if a given blockchain is valid

        :param chain: A blockchain
        :return: True if valid, False if not
        """

        last_block = chain[0]
        current_index = 1

        while current_index < len(chain):
            block = chain[current_index]
            print(f'{last_block}')
            print(f'{block}')
            print("\n-----------\n")
            # Check that the hash of the block is correct
            if block['previous_hash'] != self.hash(last_block):
                return False

            # For PoS, we don't need to validate proof as in PoW
            # Instead, we verify that the validator was legitimate
            if not self.valid_validator(block['validator']):
                return False

            last_block = block
            current_index += 1

        return True
    
    def valid_validator(self, validator):
        """
        Check if the validator is in our list of validators
        
        :param validator: The validator to check
        :return: True if valid, False if not
        """
        return validator in self.validators or validator == 'genesis'

    def resolve_conflicts(self):
        """
        This is our consensus algorithm, it resolves conflicts
        by replacing our chain with the longest one in the network.

        :return: True if our chain was replaced, False if not
        """

        neighbours = self.nodes
        new_chain = None

        # We're only looking for chains longer than ours
        max_length = len(self.chain)

        # Grab and verify the chains from all the nodes in our network
        for node in neighbours:
            response = requests.get(f'http://{node}/chain')

            if response.status_code == 200:
                length = response.json()['length']
                chain = response.json()['chain']

                # Check if the length is longer and the chain is valid
                if length > max_length and self.valid_chain(chain):
                    max_length = length
                    new_chain = chain

        # Replace our chain if we discovered a new, valid chain longer than ours
        if new_chain:
            self.chain = new_chain
            return True

        return False

    def new_block(self, previous_hash, validator):
        """
        Create a new Block in the Blockchain

        :param previous_hash: Hash of previous Block
        :param validator: The validator who created this block
        :return: New Block
        """

        block = {
            'index': len(self.chain) + 1,
            'timestamp': time(),
            'transactions': self.current_transactions,
            'validator': validator,
            'previous_hash': previous_hash or self.hash(self.chain[-1]),
        }

        # Reset the current list of transactions
        self.current_transactions = []

        self.chain.append(block)
        return block

    def new_transaction(self, sender, recipient, amount, order):
        """
        Creates a new transaction to go into the next mined Block

        :param sender: Address of the Sender
        :param recipient: Address of the Recipient
        :param amount: Amount
        :param order: Order number
        :return: The index of the Block that will hold this transaction
        """
        
        transaction = {
            'sender': sender,
            'recipient': recipient,
            'amount': amount,
            'order': order,    
        }
        
        self.current_transactions.append(transaction)

        return self.last_block['index'] + 1

    @property
    def last_block(self):
        return self.chain[-1]

    @staticmethod
    def hash(block):
        """
        Creates a SHA-256 hash of a Block

        :param block: Block
        """

        # We must make sure that the Dictionary is Ordered, or we'll have inconsistent hashes
        block_string = json.dumps(block, sort_keys=True).encode()
        return hashlib.sha256(block_string).hexdigest()

    def proof_of_stake(self):
        """
        Simple Proof of Stake Algorithm:
        - Select a validator based on their stake
        - Higher stake means higher chance of being selected

        :return: The selected validator
        """
        # Calculate total stake
        total_stake = sum(self.stakes.values())
        
        # Generate a random number between 0 and total stake
        target = random.randint(0, total_stake - 1)
        
        # Select validator based on stake weight
        cumulative_stake = 0
        for validator, stake in self.stakes.items():
            cumulative_stake += stake
            if target < cumulative_stake:
                return validator
        
        # Fallback (should not reach here)
        return list(self.stakes.keys())[0]
    
    def manipulate(self, block_index):
        """
        Manipulates a transaction in the specified node's blockchain
        
        :param node: The index of the block to manipulate
        """
        # Convert node parameter to integer
        block_index = int(block_index)
        
        # Check if the block exists
        if block_index >= len(self.chain):
            return False
            
        # Get the block
        block = self.chain[block_index]
        
        # Manipulate the first transaction in the block if it exists
        if block['transactions']:
            # Change the amount of the first transaction
            block['transactions'][0]['amount'] += 100
            
        return True


# Instantiate the Node
app = Flask(__name__)

# Generate a globally unique address for this node
node_identifier = str(uuid4()).replace('-', '')

# Instantiate the Blockchain
blockchain = Blockchain()


@app.route('/mine', methods=['GET'])
def mine():
    # We run the proof of stake algorithm to select a validator
    validator = blockchain.proof_of_stake()
    
    # We must receive a reward for validating a block
    # The sender is "0" to signify that this node has created a new coin
    blockchain.new_transaction(
        sender="0",
        recipient=node_identifier,
        amount=1,
        order=0,
    )

    # Forge the new Block by adding it to the chain
    previous_hash = blockchain.hash(blockchain.last_block)
    block = blockchain.new_block(previous_hash, validator)

    response = {
        'message': "New Block Forged",
        'index': block['index'],
        'transactions': block['transactions'],
        'validator': block['validator'],
        'previous_hash': block['previous_hash'],
    }
    return jsonify(response), 200


@app.route('/transactions/new', methods=['POST'])
def new_transaction():
    values = request.get_json()

    # Check that the required fields are in the POST'ed data
    required = ['sender', 'recipient', 'amount', 'order']
    if not all(k in values for k in required):
        return 'Missing values', 400

    # Create a new Transaction
    index = blockchain.new_transaction(values['sender'], values['recipient'], values['amount'], values['order'])

    response = {'message': f'Transaction will be added to Block {index}'}
    return jsonify(response), 201


@app.route('/chain', methods=['GET'])
def full_chain():
    response = {
        'chain': blockchain.chain,
        'length': len(blockchain.chain),
    }
    return jsonify(response), 200


@app.route('/nodes/register', methods=['POST'])
def register_nodes():
    values = request.get_json()

    nodes = values.get('nodes')
    if nodes is None:
        return "Error: Please supply a valid list of nodes", 400

    #for node in nodes:
    #    blockchain.register_node(node)
    blockchain.register_node(nodes)

    response = {
        'message': 'New nodes have been added',
        'total_nodes': list(blockchain.nodes),
    }
    return jsonify(response), 201


@app.route('/nodes/resolve', methods=['GET'])
def consensus():
    replaced = blockchain.resolve_conflicts()

    if replaced:
        response = {
            'message': 'Our chain was replaced',
            'new_chain': blockchain.chain
        }
    else:
        response = {
            'message': 'Our chain is authoritative',
            'chain': blockchain.chain
        }

    return jsonify(response), 200

@app.route('/nodes/list', methods=['GET'])
def list_nodes():
    response = {
        'nodes': list(blockchain.nodes)
    }
    return jsonify(response), 200

@app.route('/validate', methods=['GET'])
def validate():
    response = {
        'valid': blockchain.valid_chain(blockchain.chain)
    }
    return jsonify(response), 200

@app.route('/nodes/manipulate', methods=['POST'])
def manipulate():
    values = request.get_json()
    blockchain.manipulate(values['block_index'])
    return jsonify({'message': 'Block manipulated'}), 200

@app.route('/stakes', methods=['GET'])
def get_stakes():
    """
    Get the current stakes of all validators
    """
    response = {
        'stakes': blockchain.stakes
    }
    return jsonify(response), 200

@app.route('/stakes/update', methods=['POST'])
def update_stake():
    """
    Update the stake of a validator
    """
    values = request.get_json()
    
    if not all(k in values for k in ['validator', 'stake']):
        return 'Missing values', 400
    
    validator = values['validator']
    stake = int(values['stake'])
    
    blockchain.stakes[validator] = stake
    
    if validator not in blockchain.validators:
        blockchain.validators.append(validator)
    
    response = {
        'message': f'Stake updated for {validator}',
        'stakes': blockchain.stakes
    }
    return jsonify(response), 200

if __name__ == '__main__':
    from argparse import ArgumentParser

    parser = ArgumentParser()
    parser.add_argument('-p', '--port', default=5000, type=int, help='port to listen on')
    args = parser.parse_args()
    port = args.port

    app.run(host='0.0.0.0', port=port)
