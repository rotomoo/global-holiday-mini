package com.globalholidaymini.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "holiday",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"localName", "date", "countryCode"})
    },
    indexes = {
        @Index(name = "ix_holiday_date", columnList = "date"),
        @Index(name = "ix_holiday_countryCode", columnList = "countryCode"),
        @Index(name = "ix_holiday_years", columnList = "years"),
    })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String countryCode;

    @Column(nullable = false)
    private Integer years;

    private String localName;

    private String name;

    private Boolean fixed;

    private Boolean global;

    private Integer launchYear;
}