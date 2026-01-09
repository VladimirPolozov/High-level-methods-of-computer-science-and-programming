package domain.exceptions

import main.kotlin.domain.enums.ExitCode

class InvalidLoginException : RuntimeException("INVALID_LOGIN(${ExitCode.INVALID_LOGIN.code})")
class InvalidPasswordException : RuntimeException("INVALID_PASSWORD(${ExitCode.INVALID_PASSWORD.code})")
class DbConnectionException : RuntimeException("DB_CONNECTION_ERROR(${ExitCode.DB_CONNECTION_ERROR})")
