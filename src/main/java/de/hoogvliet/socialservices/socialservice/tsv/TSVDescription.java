package de.hoogvliet.socialservices.socialservice.tsv;

public enum TSVDescription {
    COLUMN_TIMESTAMP(0),
    COLUMN_NAME(1),
    COLUMN_ADRESS(2),
    COLUMN_POSTCODE(3),
    COLUMN_CITY(4),
    COLUMN_WEBSITE(5);

    private final int column;

    TSVDescription(int columNo) {
        this.column = columNo;
    }

    public int getColum() {
        return column;
    }
}
