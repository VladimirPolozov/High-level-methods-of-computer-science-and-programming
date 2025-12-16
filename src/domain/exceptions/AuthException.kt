package domain.exceptions

import domain.enums.ExitCode

class InvalidLoginException : Exception("INVALID_LOGIN(${ExitCode.INVALID_LOGIN.code})")
class InvalidPasswordException : Exception("INVALID_PASSWORD(${ExitCode.INVALID_PASSWORD.code})")
class DbConnectionException : Exception("DB_CONNECTION_ERROR(${ExitCode.DB_CONNECTION_ERROR})")
