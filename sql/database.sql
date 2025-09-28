-- Table Département

CREATE TABLE IF NOT EXISTS departement (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL
);

-- Table Agent

CREATE TABLE IF NOT EXISTS agent (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    type_agent ENUM(
        'OUVRIER',
        'RESPONSABLE_DEPARTEMENT',
        'DIRECTEUR',
        'STAGIAIRE'
    ) NOT NULL,
    departement_id INT,
    est_responsable_departement BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (departement_id) REFERENCES departement (id) ON DELETE SET NULL
);

-- Table Paiement

CREATE TABLE IF NOT EXISTS paiement (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type_paiement ENUM(
        'SALAIRE',
        'PRIME',
        'BONUS',
        'INDEMNITE'
    ) NOT NULL,
    montant DECIMAL(10, 2) NOT NULL CHECK (montant >= 0),
    date_paiement DATE NOT NULL,
    motif VARCHAR(255),
    condition_validee BOOLEAN DEFAULT FALSE,
    agent_id INT NOT NULL,
    FOREIGN KEY (agent_id) REFERENCES agent (id) ON DELETE CASCADE
);

-- Insérer le département Administration
INSERT INTO departement (nom)
VALUES ('Administration');

-- Insérer le Directeur lié à ce département
INSERT INTO agent (nom, prenom, email, mot_de_passe, type_agent, departement_id, est_responsable_departement)
VALUES ('salhi', 'meryam', 'salhi.meryam@youcode.com', 'admin123', 'DIRECTEUR', 
        (SELECT id FROM departement WHERE nom = 'Administration' LIMIT 1), TRUE);

