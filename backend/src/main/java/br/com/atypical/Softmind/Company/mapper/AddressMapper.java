package br.com.atypical.Softmind.Company.mapper;

import br.com.atypical.Softmind.Company.dto.AddressDto;
import br.com.atypical.Softmind.Company.entities.Address;

public class AddressMapper {

    public static Address toEntity(AddressDto dto) {
        if (dto == null) return null;
        return new Address(
                dto.street(),
                dto.number(),
                dto.complement(),
                dto.city(),
                dto.state(),
                dto.zip()
        );
    }

    public static AddressDto toDto(Address entity) {
        if (entity == null) return null;
        return new AddressDto(
          entity.getStreet(),
          entity.getNumber(),
          entity.getComplement(),
          entity.getCity(),
          entity.getState(),
          entity.getZip()
        );
    }
}
