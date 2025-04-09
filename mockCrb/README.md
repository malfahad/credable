# Mock Services

This project provides mock implementations of two services:
1. Central Banking Service - Port 8093
2. Scoring Service - Port 8094

## Prerequisites

- Python 3.8 or higher
- pip (Python package installer)

## Setup

1. Create a virtual environment (recommended):
```bash
python -m venv venv
source venv/bin/activate  # On Windows use: venv\Scripts\activate
```

2. Install dependencies:
```bash
pip install -r requirements.txt
```

## Running the Services

To start both services simultaneously:

```bash
python run_services.py
```

This will start:
- CBS service on `http://localhost:8093`
- Scoring service on `http://localhost:8094`

To stop the services, press `Ctrl+C` in the terminal.
