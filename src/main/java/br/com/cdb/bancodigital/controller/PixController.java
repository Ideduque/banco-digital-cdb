package br.com.cdb.bancodigital.controller;

import br.com.cdb.bancodigital.dto.PixDTO;
import br.com.cdb.bancodigital.entity.Pix;
import br.com.cdb.bancodigital.service.PixService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pix")
@RequiredArgsConstructor
public class PixController
{
    private
    final PixService pixService;

    @PostMapping("/transferir")
    public ResponseEntity<Pix> transferir(@RequestBody PixDTO dto)
    {
        Pix transacao = pixService.transferir(dto);
        return ResponseEntity.ok(transacao);
    }
}