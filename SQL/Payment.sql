CREATE TABLE Payment (
    PaymentId INT PRIMARY KEY AUTO_INCREMENT,
    OccupationId INT NOT NULL,
    PaymentDate DATE NOT NULL,
    Amount DECIMAL(10,2) NOT NULL DEFAULT 200.00,
    FOREIGN KEY (OccupationId) REFERENCES OccupationRegistration(OccupationId) ON DELETE CASCADE
);