package com.mindhub.homebanking.dtos;

import java.time.LocalDateTime;

public class TransactionPDFExportDTO {

    private long id;
    private LocalDateTime localDateTimeStart;
    private LocalDateTime localDateTimeEnd;

    public TransactionPDFExportDTO() {
    }


    public long getId() {
        return id;
    }

    public LocalDateTime getLocalDateTimeStart() {
        return localDateTimeStart;
    }

    public LocalDateTime getLocalDateTimeEnd() {
        return localDateTimeEnd;
    }


}
