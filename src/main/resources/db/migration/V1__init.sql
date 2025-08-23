CREATE TABLE users (
    uid      UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    borrow_limit INTEGER NOT NULL DEFAULT 5
);

CREATE TABLE books (
    bid              UUID PRIMARY KEY,
    title            VARCHAR(255),
    author           VARCHAR(255),
    total_copies     INTEGER NOT NULL,
    available_copies INTEGER NOT NULL
);

CREATE TABLE loans (
    loanid    UUID PRIMARY KEY,
    uid        UUID NOT NULL REFERENCES users(uid) ON DELETE CASCADE,
    bid        UUID NOT NULL REFERENCES books(bid) ON DELETE CASCADE,
    start_date DATE
);

-- fk lookup, google it
CREATE INDEX idx_loans_user ON loans(uid);
CREATE INDEX idx_loans_book ON loans(bid);