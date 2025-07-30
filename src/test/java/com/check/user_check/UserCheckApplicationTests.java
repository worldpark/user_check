package com.check.user_check;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//@SpringBootTest
//@ActiveProfiles("dev")
class UserCheckApplicationTests {

    private record TestDto(
            LocalDate examDate,
            int one,
            int two,
            int three
    ){}

    private record ResultDto(
            List<Header> headers,
            List<RowDataClass> rows

    ){}

    private record Header(
            LocalDate title,
            String subTitle
    ){}

    private class RowDataClass{
        private int dayOneCount;
        private int dayTwoCount;
        private int dayThreeCount;
        private int sum = 0;

        public RowDataClass(int dayOneCount, int dayTwoCount, int dayThreeCount) {
            this.dayOneCount = dayOneCount;
            this.dayTwoCount = dayTwoCount;
            this.dayThreeCount = dayThreeCount;
            calcSum();
        }

        public int getDayOneCount() {
            return dayOneCount;
        }

        public int getDayTwoCount() {
            return dayTwoCount;
        }

        public int getDayThreeCount() {
            return dayThreeCount;
        }

        public int getSum() {
            return sum;
        }

        public void calcSum() {
            this.sum = this.dayOneCount + this.dayTwoCount + this.dayThreeCount;
        }
    }

    @Test
    void contextLoads() {

        List<TestDto> testDataList = new ArrayList<>(
                List.of(
                        new TestDto(LocalDate.parse("2025-07-21"), 0, 10, 20),
                        new TestDto(LocalDate.parse("2025-07-22"), 5, 0, 50),
                        new TestDto(LocalDate.parse("2025-07-23"), 0, 5, 0)
                )
        );

        List<Header> headerList = new ArrayList<>();
        List<RowDataClass> result = new ArrayList<>();

        for (TestDto testDto : testDataList) {
            headerList.add(
                    new Header(
                            testDto.examDate,
                            "aa"
                    )
            );
        }

        for(int i = 0; i < 3; i++){
            int first = getHourValue(testDataList.get(0), i);
            int second = getHourValue(testDataList.get(1), i);
            int third = getHourValue(testDataList.get(2), i);

            result.add(new RowDataClass(first, second, third));
        }

        ResultDto resultDto = new ResultDto(headerList, result);

        System.out.println(resultDto);
    }

    public int getHourValue(TestDto dto, int hourIndex) {

        return switch (hourIndex) {
            case 0 -> dto.one();
            case 1 -> dto.two();
            case 2 -> dto.three();
            default -> throw new IllegalArgumentException("Invalid hour: " + hourIndex);
        };
    }

}
