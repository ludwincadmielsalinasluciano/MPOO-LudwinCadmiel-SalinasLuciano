package mx.unam.fi.smartvend.api.controller;

import mx.unam.fi.smartvend.api.dto.CompraRequest;
import mx.unam.fi.smartvend.api.dto.CompraResponse;
import mx.unam.fi.smartvend.api.dto.ErrorResponse;
import mx.unam.fi.smartvend.api.dto.FondosResponse;
import mx.unam.fi.smartvend.api.dto.ProductoResponse;
import mx.unam.fi.smartvend.api.dto.ReabastecerRequest;
import mx.unam.fi.smartvend.api.dto.RegistroProductoRequest;
import mx.unam.fi.smartvend.application.service.SmartVendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
public class SmartVendController {

    private final SmartVendService smartVendService;

    public SmartVendController(SmartVendService smartVendService) {
        this.smartVendService = smartVendService;
    }

    @PostMapping("/productos")
    public ResponseEntity<ProductoResponse> registrarProducto(@RequestBody RegistroProductoRequest request) {
        ProductoResponse response = smartVendService.registrarProducto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/productos/{codigo}")
    public ResponseEntity<ProductoResponse> buscarProducto(@PathVariable String codigo) {
        return ResponseEntity.ok(smartVendService.buscarProducto(codigo));
    }

    @PatchMapping("/productos/{codigo}/existencias")
    public ResponseEntity<ProductoResponse> reabastecerProducto(
            @PathVariable String codigo,
            @RequestBody ReabastecerRequest request) {
        return ResponseEntity.ok(smartVendService.reabastecerProducto(codigo, request.cantidad()));
    }

    // Se soporta tanto /api/producto/{codigo}/comprar (como viene en el PDF) como /api/productos/{codigo}/comprar
    @PostMapping({"/producto/{codigo}/comprar", "/productos/{codigo}/comprar"})
    public ResponseEntity<CompraResponse> comprarProducto(
            @PathVariable String codigo,
            @RequestBody CompraRequest request) {
        return ResponseEntity.ok(smartVendService.comprarProducto(codigo, request));
    }

    @GetMapping("/fondos")
    public ResponseEntity<FondosResponse> consultarFondos() {
        return ResponseEntity.ok(smartVendService.consultarFondos());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> manejarNoEncontrado(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ErrorResponse> manejarOperacionInvalida(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
    }
}
