from flask import Flask, request, jsonify
import uuid
import requests 

app = Flask(__name__)

# Store registered clients
registered_clients = {}

# Store scoring tokens
scoring_tokens = {}

# Scoring service endpoints
@app.route('/api/v1/scoring/createClient', methods=['POST'])
def create_client():
    data = request.json
    token = str(uuid.uuid4())
    
    client_data = {
        "url": data.get("url"),
        "name": data.get("name"),
        "username": data.get("username"),
        "password": data.get("password"),
        "token": token
    }
    
    registered_clients[token] = client_data
    return jsonify(client_data)

@app.route('/api/v1/scoring/initiateQueryScore/<customerNumber>', methods=['GET'])
def initiate_query_score(customerNumber):
    # Get and validate bearer token
    auth_header = request.headers.get('Authorization')
    if not auth_header or not auth_header.startswith('Bearer '):
        return jsonify({"error": "Missing or invalid authorization header"}), 401
        
    client_token = auth_header.split(' ')[1]
    if client_token not in registered_clients:
        return jsonify({"error": "Invalid client token"}), 401
    token = str(uuid.uuid4())
    scoring_tokens[token] = {
        "customerNumber": customerNumber,
        "status": "pending"
    }
    return jsonify({"token": token})

@app.route('/api/v1/scoring/queryScore/<token>', methods=['GET'])
def query_score(token):
    if token not in scoring_tokens:
        return jsonify({"error": "Invalid token"}), 404
    
    customer_number = scoring_tokens[token]["customerNumber"]
    # Get transactions from CBS service
    # Get and validate bearer token
    auth_header = request.headers.get('Authorization')
    if not auth_header or not auth_header.startswith('Bearer '):
        return jsonify({"error": "Missing or invalid authorization header"}), 401
        
    client_token = auth_header.split(' ')[1]
    if client_token not in registered_clients:
        return jsonify({"error": "Invalid client token"}), 401
    client = registered_clients[client_token]
    transactions_url = f"{client['url']}/api/transactions/{customer_number}"
    
    try:
        response = requests.get(
            transactions_url,
            auth=(client['username'], client['password'])
        )
        
        if response.status_code != 200:
            return jsonify({"error": "Failed to fetch transactions"}), response.status_code
            
        transactions = response.json()
        
        if not transactions or len(transactions) == 0:
            return jsonify({"error": "No transactions found"}), 404
            
        # Update token status since we found transactions
        scoring_tokens[token]["status"] = "completed"
            
    except Exception as e:
        return jsonify({"error": f"Error fetching transactions: {str(e)}"}), 500
    return jsonify({
        "id": 9,
        "customerNumber": customer_number,
        "score": 564,
        "limitAmount": 30000,
        "exclusion": "No Exclusion",
        "exclusionReason": "No Exclusion"
    })

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8094)