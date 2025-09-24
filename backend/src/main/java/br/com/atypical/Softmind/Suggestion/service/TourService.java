package br.com.atypical.Softmind.Suggestion.service;

import br.com.atypical.Softmind.Suggestion.dto.TourDto;
import br.com.atypical.Softmind.Suggestion.entities.Tour;
import br.com.atypical.Softmind.Suggestion.exception.RecursoNaoEncontradoException;
import br.com.atypical.Softmind.Suggestion.repository.TourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;

    public TourDto cadastrarPasseio(TourDto tourDto){
        Tour tour = new Tour();
        tour.setNameTour(tourDto.nameTour());
        tour.setTypeTour(tourDto.typeTour());
        tour.setDescriptionTour(tourDto.descriptionTour());
        tour.setPlaceTour(tourDto.placeTour());

        Tour tourSalvo = tourRepository.save(tour);

        return new TourDto(tourSalvo.getNameTour(), tourSalvo.getTypeTour(), tourSalvo.getDescriptionTour(), tourSalvo.getPlaceTour());
    }
    
    public List<TourDto> listarPasseios(){
        return tourRepository.findAll()
                .stream()
                .map(tour -> new TourDto(tour.getNameTour(), tour.getTypeTour(), tour.getDescriptionTour(), tour.getPlaceTour()))
                .collect(Collectors.toList());
    }

    public TourDto atualizarPasseio(String nameTour, TourDto tourDto){
        Tour tour = buscarPasseioPorNome(nameTour);

        if (tourDto.nameTour() != null) tour.setNameTour(tourDto.nameTour());
        if (tourDto.typeTour() != null) tour.setTypeTour(tourDto.typeTour());
        if (tourDto.descriptionTour() != null) tour.setDescriptionTour(tourDto.descriptionTour());
        if (tourDto.placeTour() != null) tour.setPlaceTour(tourDto.placeTour());

        Tour tourSalvo = tourRepository.save(tour);

        return new TourDto(tourSalvo.getNameTour(), tourSalvo.getTypeTour(), tourSalvo.getDescriptionTour(), tourSalvo.getPlaceTour());
    }

    public boolean excluirPasseio(String nameTour){
        if (tourRepository.existsById(nameTour)){
            tourRepository.deleteById(nameTour);
            return true;
        }
        return false;
    }

    private Tour buscarPasseioPorNome(String nameTour){
        return tourRepository.findById(nameTour).orElseThrow(()  -> new RecursoNaoEncontradoException("Nome não encontrado:"));

    }
}
