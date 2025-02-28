#!/bin/bash

# Kill any running Python processes
pkill python

# Create and activate virtual environment if it doesn't exist
if [ ! -d "prbc" ]; then
    echo "Creating virtual environment..."
    python -m venv prbc
    source prbc/bin/activate
    pip install -r requirements.txt
else
    source prbc/bin/activate
fi

# Run two blockchain nodes
python blockchain_pos.py -p 5000 &
python blockchain_pos.py -p 5001 &

# Wait for nodes to start
echo "Waiting for nodes to start..."
sleep 3

echo -e "\n1. Checking initial stakes..."
curl http://localhost:5000/stakes

echo -e "\n2. Updating stakes for node1 and adding a new validator..."
curl -X POST -H "Content-Type: application/json" -d '{"validator": "node1", "stake": 200}' http://localhost:5000/stakes/update
curl -X POST -H "Content-Type: application/json" -d '{"validator": "node4", "stake": 300}' http://localhost:5000/stakes/update

echo -e "\n3. Checking updated stakes..."
curl http://localhost:5000/stakes

echo -e "\n4. Writing transactions to node 1..."
curl -X POST -H "Content-Type: application/json" -d '{"sender": "A","recipient": "B", "amount": 8, "order": 1}' http://localhost:5000/transactions/new
curl -X POST -H "Content-Type: application/json" -d '{"sender": "B","recipient": "C", "amount": 5, "order": 2}' http://localhost:5000/transactions/new

echo -e "\n5. Mining (validating) block at node 1..."
curl http://localhost:5000/mine

echo -e "\n6. Viewing chain at node 1..."
curl http://localhost:5000/chain

echo -e "\n7. Mining (validating) another block at node 1..."
curl http://localhost:5000/mine

echo -e "\n8. Viewing chain at node 1..."
curl http://localhost:5000/chain

echo -e "\n9. Mining (validating) another block at node 1..."
curl http://localhost:5000/mine

echo -e "\n10. Viewing chain at node 1..."
curl http://localhost:5000/chain

echo -e "\n11. Updating stakes on node 2..."
curl -X POST -H "Content-Type: application/json" -d '{"validator": "node1", "stake": 200}' http://localhost:5001/stakes/update
curl -X POST -H "Content-Type: application/json" -d '{"validator": "node4", "stake": 300}' http://localhost:5001/stakes/update

echo -e "\n12. Mining (validating) block at node 2..."
curl http://localhost:5001/mine

echo -e "\n13. Mining (validating) another block at node 2..."
curl http://localhost:5001/mine

echo -e "\n14. Viewing chain at node 2..."
curl http://localhost:5001/chain

echo -e "\n15. Registering nodes with each other..."
curl -X POST -H "Content-Type: application/json" -d '{"nodes":"http://localhost:5001"}' http://localhost:5000/nodes/register
curl -X POST -H "Content-Type: application/json" -d '{"nodes":"http://localhost:5000"}' http://localhost:5001/nodes/register

echo -e "\n16. Resolving chains..."
echo "Node 1 resolving:"
curl http://localhost:5000/nodes/resolve
echo -e "\nNode 2 resolving:"
curl http://localhost:5001/nodes/resolve

echo -e "\n17. Validating chain before manipulation..."
curl http://localhost:5000/validate

echo -e "\n18. Manipulating block 1..."
curl -X POST -H "Content-Type: application/json" -d '{"block_index":"1"}' http://localhost:5000/nodes/manipulate

echo -e "\n19. Validating chain after manipulation..."
curl http://localhost:5000/validate

echo -e "\nDemo complete!"

# Kill any running Python processes
pkill python

# Deactivate virtual environment
deactivate 