# ZondaApiClient
Simple Client for Zonda exchange API

Early development stage

The code allows currently to get data from public and secured API. Run test classes with Your favorite IDE.

Milestones / todos:

    1. Get complete transaction list. Save to CSV (DONE)

    1A. Operate on local data (saved CSV, later maybe database) rather than request API every time application starts (DONE)
        Update local data (synchronisation) on user request only.

    2. Transactions list parametrization
        a. transactions from selected Year/time scope (some code prepared)

        b. select only FIAT-CRYPTO transactions and CRYPTO-FIAT
 
    3 .Parse numeric values and make calculations (generally done)

        a. Calculate tax base value for a particular Year (according to Polish Tax Law)
 
    4. User Interface - console

    5. CSV export - extend functionality