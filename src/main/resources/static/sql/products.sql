CREATE TABLE products (
      productId 	BIGINT AUTO_INCREMENT PRIMARY KEY,
      name       	VARCHAR(100) NOT NULL,
      description TEXT,
      price       INT NOT NULL,
      stock       INT NOT NULL DEFAULT 0,
      createdAt   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      updatedAt   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO products (name, description, price, stock)
VALUES
    ('노트북', '최신형 고성능 노트북', 1500000, 10),
    ('스마트폰', '최신형 스마트폰', 1200000, 20),
    ('무선 이어폰', '노이즈 캔슬링 지원 블루투스 이어폰', 250000, 50),
    ('키보드', '기계식 키보드', 120000, 30),
    ('모니터', '27인치 게이밍 모니터', 300000, 15);