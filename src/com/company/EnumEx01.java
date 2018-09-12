package com.company;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Stream;

public class EnumEx01 {
	public static void main(String[] args) {
		EnumSet.allOf(DayEnum.class)
				.forEach(dayEnum -> System.out.print(dayEnum + " "));
		System.out.println();

		Arrays.asList(DayEnum.values())
				.forEach(dayEnum -> System.out.print(dayEnum + " "));
		System.out.println();

		EnumSet.allOf(DayEnum.class)
				.forEach(System.out::print);
		System.out.println();

		Arrays.asList(DayEnum.values())
				.forEach(System.out::print);
		System.out.println();

		DayEnum.stream()
				.filter(dayEnum -> dayEnum.getTypeOfDay().equals("off"))
				.forEach(System.out::println);

		for (DayEnum day : DayEnum.values()) {
			System.out.print(day + " ");
		}
	}
}

enum DayEnum {
	SUN("off"),
	MON("working"),
	TUE("working"),
	WED("working"),
	THU("working"),
	FRI("working"),
	SAT("off");

	private String typeOfDay;


	DayEnum(String typeOfDay) {
		this.typeOfDay = typeOfDay;
	}

	public static Stream<DayEnum> stream() {
		return Stream.of(DayEnum.values());
	}

	public String getTypeOfDay() {
		return typeOfDay;
	}
}