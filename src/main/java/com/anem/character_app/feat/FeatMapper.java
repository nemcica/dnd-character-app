package com.anem.character_app.feat;

import com.anem.character_app.util.Mapper;
import org.springframework.stereotype.Component;

@Component
public class FeatMapper implements Mapper<Feat, FeatDto> {
    @Override
    public Feat toEntity(FeatDto featDto) {
        if(featDto == null) {
            return null;
        }

        Feat feat = new Feat();
        feat.setId(featDto.id());
        feat.setName(featDto.name());
        feat.setDescription(featDto.description());
        feat.setFeatTag(featDto.featTag());
        return feat;
    }

    @Override
    public FeatDto toDto(Feat feat) {
        if(feat == null) {
            return null;
        }

        return new FeatDto(feat.getId(), feat.getName(), feat.getDescription(), feat.getFeatTag());
    }
}
