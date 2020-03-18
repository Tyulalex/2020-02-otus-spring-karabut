package student.quiz.repository.api.dataextractor;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FilterData {

    private final String columnName;
    private final String columnValue;
}
