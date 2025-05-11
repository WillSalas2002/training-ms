package com.epam.training.service;

/*
@ExtendWith(MockitoExtension.class)
class TrainerMonthlyReportServiceImplTest {

    @Mock
    private TrainingRepository trainingSummaryRepository;

    @InjectMocks
    private TrainerMonthlyReportServiceImpl trainerMonthlyReportService;

    private TrainingSummary training1;
    private TrainingSummary training2;
    private TrainingSummary training3;

    @BeforeEach
    void setUp() {
        training1 = TrainingSummary.builder()
                .username("John.Doe")
                .firstName("John")
                .lastName("Doe")
                .status(true)
                .date(LocalDateTime.of(2024, Month.MARCH, 5, 14, 0))
                .duration(120)
                .build();

        training2 = TrainingSummary.builder()
                .username("John.Doe")
                .firstName("John")
                .lastName("Doe")
                .status(true)
                .date(LocalDateTime.of(2024, Month.MARCH, 15, 10, 0))
                .duration(90)
                .build();

        training3 = TrainingSummary.builder()
                .username("John.Doe")
                .firstName("John")
                .lastName("Doe")
                .status(true)
                .date(LocalDateTime.of(2024, Month.FEBRUARY, 22, 16, 0))
                .duration(60)
                .build();
    }

    @Test
    void testGenerateMonthlyReport_WhenTrainingsExist_ShouldReturnSummary() {
        String username = "John.Doe";
        when(trainingSummaryRepository.findByUsername(username))
                .thenReturn(List.of(training1, training2, training3));

        TrainerMonthlySummary summary = trainerMonthlyReportService.generateMonthlyReport(username);

        assertNotNull(summary);
        assertEquals(username, summary.getTrainer().getUsername());
        assertTrue(summary.getSummary().containsKey(2024));
        assertEquals(2, summary.getSummary().get(2024).size());

        // Check February summary (60 min -> 1 hour)
        assertEquals(1.0, summary.getSummary().get(2024).get(Month.FEBRUARY));

        // Check March summary (120 + 90 = 210 min -> 3.5 hours)
        assertEquals(3.5, summary.getSummary().get(2024).get(Month.MARCH));

        verify(trainingSummaryRepository, times(1)).findByUsername(username);
    }

    @Test
    void testGenerateMonthlyReport_WhenNoTrainingsExist_ShouldThrowException() {
        when(trainingSummaryRepository.findByUsername("John.Doe"))
                .thenReturn(Collections.emptyList());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                trainerMonthlyReportService.generateMonthlyReport("John.Doe"));

        assertEquals("John.Doe", exception.getMessage());

        verify(trainingSummaryRepository, times(1)).findByUsername("John.Doe");
    }

    @Test
    void testGenerateMonthlyReport_WhenRepositoryReturnsNull_ShouldThrowException() {
        when(trainingSummaryRepository.findByUsername("John.Doe")).thenReturn(null);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                trainerMonthlyReportService.generateMonthlyReport("John.Doe"));

        assertEquals("John.Doe", exception.getMessage());

        verify(trainingSummaryRepository, times(1)).findByUsername("John.Doe");
    }

    @Test
    void testGenerateMonthlyReport_WhenDifferentYearsExist_ShouldGroupByYear() {
        String username = "John.Doe";
        TrainingSummary training4 = TrainingSummary.builder()
                .username(username)
                .firstName("John")
                .lastName("Doe")
                .status(true)
                .date(LocalDateTime.of(2023, Month.DECEMBER, 1, 9, 0))
                .duration(180)
                .build();

        when(trainingSummaryRepository.findByUsername(username))
                .thenReturn(List.of(training1, training2, training3, training4));

        TrainerMonthlySummary summary = trainerMonthlyReportService.generateMonthlyReport(username);

        assertNotNull(summary);
        assertEquals(username, summary.getTrainer().getUsername());
        assertEquals(2, summary.getSummary().size()); // 2023 and 2024

        assertEquals(3.0, summary.getSummary().get(2023).get(Month.DECEMBER));
        assertEquals(3.5, summary.getSummary().get(2024).get(Month.MARCH));
        assertEquals(1.0, summary.getSummary().get(2024).get(Month.FEBRUARY));

        verify(trainingSummaryRepository, times(1)).findByUsername(username);
    }

    @Test
    void testGenerateMonthlyReport_WhenTrainingsAreFromSameMonth_ShouldSumHoursCorrectly() {
        String username = "John.Doe";
        TrainingSummary training4 = TrainingSummary.builder()
                .username(username)
                .firstName("John")
                .lastName("Doe")
                .status(true)
                .date(LocalDateTime.of(2024, Month.MARCH, 20, 18, 0))
                .duration(30)
                .build();

        when(trainingSummaryRepository.findByUsername(username))
                .thenReturn(List.of(training1, training2, training4));

        TrainerMonthlySummary summary = trainerMonthlyReportService.generateMonthlyReport(username);

        assertNotNull(summary);
        assertEquals(username, summary.getTrainer().getUsername());
        assertEquals(1, summary.getSummary().get(2024).size()); // 2024

        // Check March summary (120 + 90 + 30 = 240 min -> 4 hours)
        assertEquals(4.0, summary.getSummary().get(2024).get(Month.MARCH));

        verify(trainingSummaryRepository, times(1)).findByUsername(username);
    }
}
 */
