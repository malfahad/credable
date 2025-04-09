from datetime import datetime
from flask import Flask, request, Response
import xml.etree.ElementTree as ET

app = Flask(__name__)

# Customer service
@app.route('/cbs/customerService', methods=['POST'])
def customer_service():
    # Parse the SOAP request
    root = ET.fromstring(request.data)

    customer_number = root.find('.//{http://credable.io/cbs/customer}customerNumber').text
    
    # Create response
    response = {
        'customerNumber': customer_number,
        'firstName': 'John',
        'lastName': 'Doe',
        'email': 'john.doe@example.com',
        'mobile': '1234567890',
        'monthlyIncome': 5000.0,
        'status': 'ACTIVE',
        'gender': 'MALE',
        'idType': 'NATIONAL_ID',
        'idNumber': '123456789',
        'dob': datetime(1990, 1, 1)
    }


    # Create SOAP response
    soap_response = f"""<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"><SOAP-ENV:Header/><SOAP-ENV:Body><ns2:CustomerResponse xmlns:ns2="http://credable.io/cbs/customer"><ns2:customer><ns2:customerNumber>{response['customerNumber']}</ns2:customerNumber><ns2:firstName>{response['firstName']}</ns2:firstName><ns2:lastName>{response['lastName']}</ns2:lastName><ns2:email>{response['email']}</ns2:email><ns2:mobile>{response['mobile']}</ns2:mobile><ns2:monthlyIncome>{response['monthlyIncome']}</ns2:monthlyIncome><ns2:status>{response['status']}</ns2:status><ns2:gender>{response['gender']}</ns2:gender><ns2:idType>{response['idType']}</ns2:idType><ns2:idNumber>{response['idNumber']}</ns2:idNumber><ns2:dob>{response['dob'].strftime('%Y-%m-%dT%H:%M:%S.000Z')}</ns2:dob></ns2:customer></ns2:CustomerResponse></SOAP-ENV:Body></SOAP-ENV:Envelope>"""
    
    res = Response(soap_response, mimetype='text/xml')

    return res

# Transaction service
@app.route('/cbs/transactionService', methods=['POST'])
def transaction_service():
    # Parse the SOAP request
    root = ET.fromstring(request.data)
    customer_number = root.find('.//{http://credable.io/cbs/transaction}customerNumber').text
    
    # Create response
    response = [{
        'accountNumber': '1234567890',
        'alternativechanneltrnscrAmount': 0.0,
        'alternativechanneltrnscrNumber': 0,
        'alternativechanneltrnsdebitAmount': 0.0,
        'alternativechanneltrnsdebitNumber': 0,
        'atmTransactionsNumber': 0,
        'atmtransactionsAmount': 0.0,
        'bouncedChequesDebitNumber': 0,
        'bouncedchequescreditNumber': 0,
        'bouncedchequetransactionscrAmount': 0.0,
        'bouncedchequetransactionsdrAmount': 0.0,
        'chequeDebitTransactionsAmount': 0.0,
        'chequeDebitTransactionsNumber': 0,
        'credittransactionsAmount': 0.0,
        'debitcardpostransactionsAmount': 0.0,
        'debitcardpostransactionsNumber': 0,
        'fincominglocaltransactioncrAmount': 0.0,
        'incominginternationaltrncrAmount': 0.0,
        'incominginternationaltrncrNumber': 0,
        'incominglocaltransactioncrNumber': 0,
        'lastTransactionDate': datetime.now(),
        'lastTransactionType': 'CREDIT',
        'lastTransactionValue': 500,
        'maxAtmTransactions': 0.0,
        'maxMonthlyBebitTransactions': 0.0,
        'maxalternativechanneltrnscr': 0.0,
        'maxalternativechanneltrnsdebit': 0.0,
        'maxbouncedchequetransactionscr': 0.0,
        'maxchequedebittransactions': 0.0,
        'maxdebitcardpostransactions': 0.0,
        'maxincominginternationaltrncr': 0.0,
        'maxincominglocaltransactioncr': 0.0,
        'maxmobilemoneycredittrn': 0.0,
        'maxmobilemoneydebittransaction': 0.0,
        'maxmonthlycredittransactions': 0.0,
        'maxoutgoinginttrndebit': 0.0,
        'maxoutgoinglocaltrndebit': 0.0,
        'maxoverthecounterwithdrawals': 0.0,
        'minAtmTransactions': 0.0,
        'minMonthlyDebitTransactions': 0.0,
        'minalternativechanneltrnscr': 0.0,
        'minalternativechanneltrnsdebit': 0.0,
        'minbouncedchequetransactionscr': 0.0,
        'minchequedebittransactions': 0.0,
        'mindebitcardpostransactions': 0.0,
        'minincominginternationaltrncr': 0.0,
        'minincominglocaltransactioncr': 0.0,
        'minmobilemoneycredittrn': 0.0,
        'minmobilemoneydebittransaction': 0.0,
        'minmonthlycredittransactions': 0.0,
        'minoutgoinginttrndebit': 0.0,
        'minoutgoinglocaltrndebit': 0.0,
        'minoverthecounterwithdrawals': 0.0,
        'mobilemoneycredittransactionAmount': 0.0,
        'mobilemoneycredittransactionNumber': 0,
        'mobilemoneydebittransactionAmount': 0.0,
        'mobilemoneydebittransactionNumber': 0,
        'monthlyBalance': 10000.0,
        'monthlydebittransactionsAmount': 0.0,
        'outgoinginttransactiondebitAmount': 0.0,
        'outgoinginttrndebitNumber': 0,
        'outgoinglocaltransactiondebitAmount': 0.0,
        'outgoinglocaltransactiondebitNumber': 0,
        'overdraftLimit': 0.0,
        'overthecounterwithdrawalsAmount': 0.0,
        'overthecounterwithdrawalsNumber': 0,
        'transactionValue': 500.0
    }]
    
    # Create SOAP response
    transactions_xml = ""
    for trans in response:
        transactions_xml += f"""<ns2:transactions><ns2:accountNumber>{trans['accountNumber']}</ns2:accountNumber><ns2:alternativechanneltrnscrAmount>{trans['alternativechanneltrnscrAmount']}</ns2:alternativechanneltrnscrAmount><ns2:alternativechanneltrnscrNumber>{trans['alternativechanneltrnscrNumber']}</ns2:alternativechanneltrnscrNumber><ns2:alternativechanneltrnsdebitAmount>{trans['alternativechanneltrnsdebitAmount']}</ns2:alternativechanneltrnsdebitAmount><ns2:alternativechanneltrnsdebitNumber>{trans['alternativechanneltrnsdebitNumber']}</ns2:alternativechanneltrnsdebitNumber><ns2:atmTransactionsNumber>{trans['atmTransactionsNumber']}</ns2:atmTransactionsNumber><ns2:atmtransactionsAmount>{trans['atmtransactionsAmount']}</ns2:atmtransactionsAmount><ns2:bouncedChequesDebitNumber>{trans['bouncedChequesDebitNumber']}</ns2:bouncedChequesDebitNumber><ns2:bouncedchequescreditNumber>{trans['bouncedchequescreditNumber']}</ns2:bouncedchequescreditNumber><ns2:bouncedchequetransactionscrAmount>{trans['bouncedchequetransactionscrAmount']}</ns2:bouncedchequetransactionscrAmount><ns2:bouncedchequetransactionsdrAmount>{trans['bouncedchequetransactionsdrAmount']}</ns2:bouncedchequetransactionsdrAmount><ns2:chequeDebitTransactionsAmount>{trans['chequeDebitTransactionsAmount']}</ns2:chequeDebitTransactionsAmount><ns2:chequeDebitTransactionsNumber>{trans['chequeDebitTransactionsNumber']}</ns2:chequeDebitTransactionsNumber><ns2:credittransactionsAmount>{trans['credittransactionsAmount']}</ns2:credittransactionsAmount><ns2:debitcardpostransactionsAmount>{trans['debitcardpostransactionsAmount']}</ns2:debitcardpostransactionsAmount><ns2:debitcardpostransactionsNumber>{trans['debitcardpostransactionsNumber']}</ns2:debitcardpostransactionsNumber><ns2:fincominglocaltransactioncrAmount>{trans['fincominglocaltransactioncrAmount']}</ns2:fincominglocaltransactioncrAmount><ns2:incominginternationaltrncrAmount>{trans['incominginternationaltrncrAmount']}</ns2:incominginternationaltrncrAmount><ns2:incominginternationaltrncrNumber>{trans['incominginternationaltrncrNumber']}</ns2:incominginternationaltrncrNumber><ns2:incominglocaltransactioncrNumber>{trans['incominglocaltransactioncrNumber']}</ns2:incominglocaltransactioncrNumber><ns2:lastTransactionDate>{trans['lastTransactionDate'].strftime('%Y-%m-%dT%H:%M:%S.000Z')}</ns2:lastTransactionDate><ns2:lastTransactionType>{trans['lastTransactionType']}</ns2:lastTransactionType><ns2:lastTransactionValue>{trans['lastTransactionValue']}</ns2:lastTransactionValue><ns2:maxAtmTransactions>{trans['maxAtmTransactions']}</ns2:maxAtmTransactions><ns2:maxMonthlyBebitTransactions>{trans['maxMonthlyBebitTransactions']}</ns2:maxMonthlyBebitTransactions><ns2:maxalternativechanneltrnscr>{trans['maxalternativechanneltrnscr']}</ns2:maxalternativechanneltrnscr><ns2:maxalternativechanneltrnsdebit>{trans['maxalternativechanneltrnsdebit']}</ns2:maxalternativechanneltrnsdebit><ns2:maxbouncedchequetransactionscr>{trans['maxbouncedchequetransactionscr']}</ns2:maxbouncedchequetransactionscr><ns2:maxchequedebittransactions>{trans['maxchequedebittransactions']}</ns2:maxchequedebittransactions><ns2:maxdebitcardpostransactions>{trans['maxdebitcardpostransactions']}</ns2:maxdebitcardpostransactions><ns2:maxincominginternationaltrncr>{trans['maxincominginternationaltrncr']}</ns2:maxincominginternationaltrncr><ns2:maxincominglocaltransactioncr>{trans['maxincominglocaltransactioncr']}</ns2:maxincominglocaltransactioncr><ns2:maxmobilemoneycredittrn>{trans['maxmobilemoneycredittrn']}</ns2:maxmobilemoneycredittrn><ns2:maxmobilemoneydebittransaction>{trans['maxmobilemoneydebittransaction']}</ns2:maxmobilemoneydebittransaction><ns2:maxmonthlycredittransactions>{trans['maxmonthlycredittransactions']}</ns2:maxmonthlycredittransactions><ns2:maxoutgoinginttrndebit>{trans['maxoutgoinginttrndebit']}</ns2:maxoutgoinginttrndebit><ns2:maxoutgoinglocaltrndebit>{trans['maxoutgoinglocaltrndebit']}</ns2:maxoutgoinglocaltrndebit><ns2:maxoverthecounterwithdrawals>{trans['maxoverthecounterwithdrawals']}</ns2:maxoverthecounterwithdrawals><ns2:minAtmTransactions>{trans['minAtmTransactions']}</ns2:minAtmTransactions><ns2:minMonthlyDebitTransactions>{trans['minMonthlyDebitTransactions']}</ns2:minMonthlyDebitTransactions><ns2:minalternativechanneltrnscr>{trans['minalternativechanneltrnscr']}</ns2:minalternativechanneltrnscr><ns2:minalternativechanneltrnsdebit>{trans['minalternativechanneltrnsdebit']}</ns2:minalternativechanneltrnsdebit><ns2:minbouncedchequetransactionscr>{trans['minbouncedchequetransactionscr']}</ns2:minbouncedchequetransactionscr><ns2:minchequedebittransactions>{trans['minchequedebittransactions']}</ns2:minchequedebittransactions><ns2:mindebitcardpostransactions>{trans['mindebitcardpostransactions']}</ns2:mindebitcardpostransactions><ns2:minincominginternationaltrncr>{trans['minincominginternationaltrncr']}</ns2:minincominginternationaltrncr><ns2:minincominglocaltransactioncr>{trans['minincominglocaltransactioncr']}</ns2:minincominglocaltransactioncr><ns2:minmobilemoneycredittrn>{trans['minmobilemoneycredittrn']}</ns2:minmobilemoneycredittrn><ns2:minmobilemoneydebittransaction>{trans['minmobilemoneydebittransaction']}</ns2:minmobilemoneydebittransaction><ns2:minmonthlycredittransactions>{trans['minmonthlycredittransactions']}</ns2:minmonthlycredittransactions><ns2:minoutgoinginttrndebit>{trans['minoutgoinginttrndebit']}</ns2:minoutgoinginttrndebit><ns2:minoutgoinglocaltrndebit>{trans['minoutgoinglocaltrndebit']}</ns2:minoutgoinglocaltrndebit><ns2:minoverthecounterwithdrawals>{trans['minoverthecounterwithdrawals']}</ns2:minoverthecounterwithdrawals><ns2:mobilemoneycredittransactionAmount>{trans['mobilemoneycredittransactionAmount']}</ns2:mobilemoneycredittransactionAmount><ns2:mobilemoneycredittransactionNumber>{trans['mobilemoneycredittransactionNumber']}</ns2:mobilemoneycredittransactionNumber><ns2:mobilemoneydebittransactionAmount>{trans['mobilemoneydebittransactionAmount']}</ns2:mobilemoneydebittransactionAmount><ns2:mobilemoneydebittransactionNumber>{trans['mobilemoneydebittransactionNumber']}</ns2:mobilemoneydebittransactionNumber><ns2:monthlyBalance>{trans['monthlyBalance']}</ns2:monthlyBalance><ns2:monthlydebittransactionsAmount>{trans['monthlydebittransactionsAmount']}</ns2:monthlydebittransactionsAmount><ns2:outgoinginttransactiondebitAmount>{trans['outgoinginttransactiondebitAmount']}</ns2:outgoinginttransactiondebitAmount><ns2:outgoinginttrndebitNumber>{trans['outgoinginttrndebitNumber']}</ns2:outgoinginttrndebitNumber><ns2:outgoinglocaltransactiondebitAmount>{trans['outgoinglocaltransactiondebitAmount']}</ns2:outgoinglocaltransactiondebitAmount><ns2:outgoinglocaltransactiondebitNumber>{trans['outgoinglocaltransactiondebitNumber']}</ns2:outgoinglocaltransactiondebitNumber><ns2:overdraftLimit>{trans['overdraftLimit']}</ns2:overdraftLimit><ns2:overthecounterwithdrawalsAmount>{trans['overthecounterwithdrawalsAmount']}</ns2:overthecounterwithdrawalsAmount><ns2:overthecounterwithdrawalsNumber>{trans['overthecounterwithdrawalsNumber']}</ns2:overthecounterwithdrawalsNumber><ns2:transactionValue>{trans['transactionValue']}</ns2:transactionValue></ns2:transactions>"""
    
    soap_response = f"""<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"><SOAP-ENV:Header/><SOAP-ENV:Body><ns2:TransactionsResponse xmlns:ns2="http://credable.io/cbs/transaction">{transactions_xml}</ns2:TransactionsResponse></SOAP-ENV:Body></SOAP-ENV:Envelope>"""
    
    res = Response(soap_response, mimetype='text/xml')
    return res

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8093) 