import subprocess
import os
import signal
import sys

def run_services():
    # Start CBS service
    cbs_process = subprocess.Popen([
        'gunicorn',
        '--bind', '127.0.0.1:8093',
        '--workers', '1',
        '--timeout', '120',
        '--log-level', 'info',
        '--access-logfile', '-',
        '--error-logfile', '-',
        'cbs_service:app'
    ])

    # Start Scoring service
    scoring_process = subprocess.Popen([
        'gunicorn',
        '--bind', '127.0.0.1:8094',
        '--workers', '1',
        '--timeout', '120',
        '--log-level', 'info',
        '--access-logfile', '-',
        '--error-logfile', '-',
        'scoring_service:app'
    ])

    def signal_handler(sig, frame):
        print('Shutting down services...')
        cbs_process.terminate()
        scoring_process.terminate()
        sys.exit(0)

    signal.signal(signal.SIGINT, signal_handler)
    signal.signal(signal.SIGTERM, signal_handler)

    print('Services started:')
    print('CBS Service running on http://127.0.0.1:8093')
    print('Scoring Service running on http://127.0.0.1:8094')
    print('Press Ctrl+C to stop services')

    # Keep the script running
    cbs_process.wait()
    scoring_process.wait()

if __name__ == '__main__':
    run_services() 