package br.com.atypical.Softmind.PhysicalActivity.controller;

import br.com.atypical.Softmind.PhysicalActivity.dto.PhysicalActDto;
import br.com.atypical.Softmind.PhysicalActivity.service.PhysicalActService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/suggestion/physical-activities")
@RequiredArgsConstructor
public class PhysicalActController {

    private final PhysicalActService physicalActService;

    @GetMapping("/atividadesfisicas")
    public List<PhysicalActDto> getVideosDeAtividades() {
        return physicalActService.buscarVideosAtividadeFisica();
    }
}
