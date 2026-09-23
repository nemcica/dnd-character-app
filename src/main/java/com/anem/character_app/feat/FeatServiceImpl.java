package com.anem.character_app.feat;

import com.anem.character_app.util.Mapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class FeatServiceImpl implements FeatService{

    private final Mapper<Feat, FeatDto> featMapper;
    private final FeatRepository featRepository;

    @Override
    public FeatDto createFeat(FeatDto featDto) {
        if(featRepository.existsById(featDto.id())) {
            throw new EntityExistsException("Id: " + featDto.id() + " already exists.");
        }
        Feat savedFeat = featMapper.toEntity(featDto);
        return featMapper.toDto(featRepository.save(savedFeat));
    }

    @Override
    public FeatDto updateFeat(String id, FeatDto featDto) {
        if(!featRepository.existsById(id)) {
            throw new EntityNotFoundException("Id: " + featDto.id() + " does not exist.");
        }
        Feat savedFeat = featMapper.toEntity(featDto);
        return featMapper.toDto(featRepository.save(savedFeat));
    }

    @Override
    public Page<FeatDto> getAllFeats(Pageable pageable) {
        return featRepository.findAll(pageable).map(featMapper::toDto);
    }

    @Override
    public Optional<FeatDto> getFeatById(String id) {
        return featRepository.findById(id).map(featMapper::toDto);
    }

    @Override
    public boolean exists(String id) {
        return featRepository.existsById(id);
    }

    @Override
    public void delete(String id) {
        featRepository.deleteById(id);
    }
}
