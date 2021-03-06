package com.github.haseoo.courier.repositories.adapters;

import com.github.haseoo.courier.models.MagazineModel;
import com.github.haseoo.courier.models.ParcelStateRecord;
import com.github.haseoo.courier.repositories.jpa.MagazineJPARepository;
import com.github.haseoo.courier.repositories.ports.MagazineRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.github.haseoo.courier.testutlis.constants.Constants.EXPECTED_LIST_SIZE_TWO_ELEMENTS;
import static com.github.haseoo.courier.testutlis.constants.Constants.INTEGRATION_TEST;
import static com.github.haseoo.courier.testutlis.generators.MagazineDataGenerator.getInactiveMagazineModel;
import static com.github.haseoo.courier.testutlis.generators.MagazineDataGenerator.getMagazineModel;
import static com.github.haseoo.courier.testutlis.generators.ParcelDataGenerator.getTestMagazineRecordModel;

@SpringBootTest
@Tag(INTEGRATION_TEST)
class MagazineRepositoryTest extends TestsWithParcels {
    @Autowired
    MagazineRepository sut;
    @Autowired
    MagazineJPARepository magazineJPARepository;

    @BeforeEach
    void setup() {
        magazineJPARepository.deleteAll();
        super.setup();
    }

    @AfterEach
    void tearDown() {
        magazineJPARepository.deleteAll();
        super.tearDown();
    }

    @Test
    void should_add_magazine() {
        //given
        MagazineModel in = getMagazineModel();
        //when
        MagazineModel out = sut.saveAndFlush(in);
        //then
        Assertions.assertThat(in).isEqualTo(out);
    }

    @Test
    void should_edit_Magazine() {
        //given
        MagazineModel entity = magazineJPARepository.saveAndFlush(getMagazineModel());
        entity.setActive(false);
        //when
        MagazineModel out = sut.saveAndFlush(entity);
        //then
        Assertions.assertThat(out.getActive()).isFalse();
    }

    @Test
    void should_return_list_with_two_element() {
        //given
        magazineJPARepository.saveAndFlush(getMagazineModel());
        magazineJPARepository.saveAndFlush(getMagazineModel());
        //when & then
        Assertions.assertThat(sut.getList()).hasSize(EXPECTED_LIST_SIZE_TWO_ELEMENTS);
    }

    @Test
    void should_return_list_with_active_elements() {
        //given
        magazineJPARepository.saveAndFlush(getMagazineModel());
        magazineJPARepository.saveAndFlush(getMagazineModel());
        magazineJPARepository.saveAndFlush(getInactiveMagazineModel());
        magazineJPARepository.saveAndFlush(getInactiveMagazineModel());
        //when & then
        Assertions.assertThat(sut.getActiveMagazines()).allMatch(MagazineModel::getActive);
    }

    @Transactional
    @Test
    void should_assign_parcel_to_magazine() {
        //given
        MagazineModel magazine = magazineJPARepository.saveAndFlush(getMagazineModel());
        ParcelStateRecord parcelStateRecord = getTestMagazineRecordModel(parcelModel, magazine);
        //when
        magazine.getParcelStates().add(parcelStateRecord);
        MagazineModel edited = sut.saveAndFlush(magazine);
        //then
        Assertions.assertThat(edited.getParcelStates()).first()
                .extracting(stateRecord -> stateRecord.getMagazine().getId())
                .isEqualTo(magazine.getId());
        Assertions.assertThat(edited.getParcelStates()).first()
                .extracting(stateRecord -> stateRecord.getParcel().getId())
                .isEqualTo(parcelModel.getId());
    }
}