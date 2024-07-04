package com.hacken.exception;

import java.time.LocalDateTime;

public record CustomError(
        String path,
        String message,
        int statusCode,
        LocalDateTime localDateTime
) {
}
