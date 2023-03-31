package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPA.MPAStorage;

import java.util.List;

@Service
public class MpaService {
    private final MPAStorage mpaStorage;

    @Autowired
    public MpaService(MPAStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<MPA> getAll() {
        return mpaStorage.getAll();
    }

    public MPA get(long mpaId) {
        validateId(mpaId);
        return mpaStorage.get(mpaId);
    }

    private void validateId(Long id) {
        if (mpaStorage.get(id) == null || id < 0) {
            throw new DataNotFoundException("MPA с таким id не найден");
        }
    }

}
