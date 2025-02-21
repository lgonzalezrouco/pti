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
python blockchain.py -p 5000 &
python blockchain.py -p 5001 &

# Wait for nodes to start
echo "Waiting for nodes to start..."
sleep 3

echo -e "\n1. Writing transactions to node 1..."
curl -X POST -H "Content-Type: application/json" -d '{"sender": "A","recipient": "B", "amount": 8, "order": 1}' http://localhost:5000/transactions/new
curl -X POST -H "Content-Type: application/json" -d '{"sender": "B","recipient": "C", "amount": 5, "order": 2}' http://localhost:5000/transactions/new

echo -e "\n2. Mining first block at node 1..."
curl http://localhost:5000/mine

echo -e "\n3. Viewing chain at node 1..."
curl http://localhost:5000/chain

echo -e "\n4. Mining second block at node 1..."
curl http://localhost:5000/mine

echo -e "\n5. Viewing chain at node 1..."
curl http://localhost:5000/chain

echo -e "\n6. Mining third block at node 1..."
curl http://localhost:5000/mine

echo -e "\n7. Viewing chain at node 1..."
curl http://localhost:5000/chain

echo -e "\n8. Mining first block at node 2..."
curl http://localhost:5001/mine

echo -e "\n9. Mining second block at node 2..."
curl http://localhost:5001/mine

echo -e "\n10. Viewing chain at node 2..."
curl http://localhost:5001/chain

echo -e "\n11. Registering nodes with each other..."
curl -X POST -H "Content-Type: application/json" -d '{"nodes":"http://localhost:5001"}' http://localhost:5000/nodes/register
curl -X POST -H "Content-Type: application/json" -d '{"nodes":"http://localhost:5000"}' http://localhost:5001/nodes/register

echo -e "\n12. Resolving chains..."
echo "Node 1 resolving:"
curl http://localhost:5000/nodes/resolve
echo -e "\nNode 2 resolving:"
curl http://localhost:5001/nodes/resolve

echo -e "\n13. Validating chain before manipulation..."
curl http://localhost:5000/validate

echo -e "\n14. Manipulating block 1..."
curl -X POST -H "Content-Type: application/json" -d '{"block_index":"1"}' http://localhost:5000/nodes/manipulate

echo -e "\n15. Validating chain after manipulation..."
curl http://localhost:5000/validate

echo -e "\nDemo complete!"

deactivate

# Kill any running Python processes
pkill python