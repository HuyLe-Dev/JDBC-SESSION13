package com.example.session13.exercise02;

import java.time.LocalDate;

// record = readonly interface trong TS
public record Student(int studentId, String fullName, LocalDate dateOfBirth, String email) {}