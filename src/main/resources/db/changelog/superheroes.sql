CREATE TABLE SUPERHEROES(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(25) NOT NULL,
    power VARCHAR(50) NOT NULL
);

INSERT INTO SUPERHEROES(name, power) VALUES
    ('Superman', 'God'),
    ('Batman', 'Rich'),
    ('Wonder Woman', 'Super fighter'),
    ('Spider-Man', 'Spider things'),
    ('Captain America', 'Test tube boy & strength'),
    ('Thor', 'The lightning boy'),
    ('Flash', 'The fastest runner'),
    ('Hulk', 'Always furious big green boy'),
    ('Iron-Man', 'Geek tech boy'),
    ('Aquaman', 'Fish boy');