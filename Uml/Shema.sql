CREATE TABLE Wallet (
    id CHAR(25) PRIMARY KEY,
    address VARCHAR(255) NOT NULL,
    solde DOUBLE NOT NULL DEFAULT 0
);

CREATE TABLE WalletBitcoin (
   wallet_id CHAR(25) PRIMARY KEY,
   satoshi_byte DOUBLE NOT NULL,
   CONSTRAINT fk_wallet_bitcoin FOREIGN KEY (wallet_id) REFERENCES Wallet(id) ON DELETE CASCADE
);

CREATE TABLE WalletEthereum (
    wallet_id CHAR(25) PRIMARY KEY,
    prix_gas DOUBLE NOT NULL,
    limite_gas INT NOT NULL,
    CONSTRAINT fk_wallet_ethereum FOREIGN KEY (wallet_id) REFERENCES Wallet(id) ON DELETE CASCADE
);

CREATE TABLE Transaction (
    id CHAR(25) PRIMARY KEY,
    wallet_id CHAR(25) NOT NULL,
    montant DOUBLE NOT NULL,
    source VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    datee DATETIME NOT NULL,
    frais DOUBLE NOT NULL,
    taille_bytes INT NOT NULL,
    priority ENUM('ECONOMIQUE', 'STANDARD', 'RAPIDE') NOT NULL DEFAULT 'STANDARD',
    status ENUM('PENDING', 'CONFIRMED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    CONSTRAINT fk_transaction_wallet FOREIGN KEY (wallet_id) REFERENCES Wallet(id) ON DELETE CASCADE
);