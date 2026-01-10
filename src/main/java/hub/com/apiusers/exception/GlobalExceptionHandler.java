package hub.com.apiusers.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Jakarta Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(HttpServletRequest req, MethodArgumentNotValidException mne){

        //filter msg
        String errorMsg = mne.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": "+e.getDefaultMessage())
                .collect(Collectors.joining(" | "));

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                errorMsg,
                req.getRequestURI(),
                "Method Argument Not Valid"
        );
        log.warn("BussinessException {} : validation ", req.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Resource not found 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(HttpServletRequest req, ResourceNotFoundException rne){
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND,
                rne.getMessage(),
                req.getRequestURI(),
                "Resource Not Found"
        );
        log.warn("BussinessException {} : value not found", req.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Unique Exception
    @ExceptionHandler(UniqueException.class)
    public ResponseEntity<ErrorResponse> handleUniqueException(HttpServletRequest req, UniqueException ue){
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT,
                ue.getMessage(),
                req.getRequestURI(),
                "Unique Exception"
        );
        log.warn("UniqueException at {} : {}", req.getRequestURI(), ue.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }


    // others exception

    // Bad format JSON 400
    @ExceptionHandler (HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "json bad format.",
                req.getRequestURI(),
                "MalformedJsonError"
        );
        log.warn("BusinessException en {}: value not readable", req.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Error Internal SQL
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDatabase(DataAccessException ex, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error en la base de datos",
                request.getRequestURI(),
                "DatabaseError"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    // 🔒 2.6 - Acceso denegado (403)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.FORBIDDEN,
                "Acceso denegado",
                request.getRequestURI(),
                "AccessDenied"
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // ❗ 2.7 - Genérico para errores no controlados (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor",
                request.getRequestURI(),
                "InternalServerError"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
