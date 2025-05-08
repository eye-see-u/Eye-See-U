package me.eyeseeu.kiosk.option.repository;

import java.util.List;
import me.eyeseeu.kiosk.option.entity.Option;
import me.eyeseeu.kiosk.option.entity.OptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {

    List<Option> findByOptionGroup(OptionGroup optionGroup);
}
