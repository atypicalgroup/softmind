package br.com.atypical.Softmind.Support.mapper;

import br.com.atypical.Softmind.Support.dto.SupportCreateDto;
import br.com.atypical.Softmind.Support.dto.SupportDto;
import br.com.atypical.Softmind.Support.entities.Support;

public class SupportMapper {

    public static Support toEntity(SupportCreateDto dto) {
        if (dto == null) return null;

        Support support = new Support();
        support.setName(dto.name());
        support.setDescription(dto.description());
        support.setContactNumber(dto.contactNumber());
        return support;
    }

    public static SupportDto toDto(Support entity) {
        if (entity == null) return null;

        return new SupportDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getContactNumber()
        );
    }
}
