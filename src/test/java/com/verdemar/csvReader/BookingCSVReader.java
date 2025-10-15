package com.verdemar.csvReader;

import com.opencsv.bean.CsvToBeanBuilder;
import com.verdemar.domain.booking.BookingDto;
import com.verdemar.domain.csv.BookingRow;
import com.verdemar.repository.ApartmentRepository;
import com.verdemar.repository.ClientRepository;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/** Utility class for reading initial prices from a CSV file and saving them to the database. */
@Service
public class BookingCSVReader {

  private final ModelMapper modelMapper;
  private final ClientRepository clientRepository;
  private final ApartmentRepository apartmentRepository;

  public BookingCSVReader(
      ModelMapper modelMapper,
      ClientRepository clientRepository,
      ApartmentRepository apartmentRepository) {
    this.modelMapper = modelMapper;
    this.clientRepository = clientRepository;
    this.apartmentRepository = apartmentRepository;
  }

  public List<BookingDto> bookingCSVReader(String path) {
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {

      if (inputStream == null) {
        System.err.println("⚠️ CSV file not found: " + path);
        return List.of();
      }

      InputStreamReader reader = new InputStreamReader(inputStream);

      List<BookingRow> bookingRows =
          new CsvToBeanBuilder<BookingRow>(reader)
              .withType(BookingRow.class)
              .withIgnoreLeadingWhiteSpace(true)
              .build()
              .parse();

      System.out.println("✅ Found rows: " + bookingRows.size());

      List<BookingDto> bookingsDTO = new java.util.ArrayList<>();

      for (BookingRow bookingRow : bookingRows) {
        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(bookingRow.getId());
        bookingDto.setClientId(bookingRow.getClientId());
        bookingDto.setGuests(bookingRow.getGuests());
        bookingDto.setApartmentId(bookingRow.getApartmentId());
        bookingDto.setStartDate(bookingRow.getStartDate());
        bookingDto.setEndDate(
            bookingRow.getStartDate().plusDays(ThreadLocalRandom.current().nextInt(5, 16)));
        bookingDto.setStatus(bookingRow.getStatus());
        bookingDto.setNotes(bookingRow.getNotes());

        bookingsDTO.add(bookingDto);
      }

      return bookingsDTO;

    } catch (Exception e) {
      // e.printStackTrace();
      // return List.of();
      throw new RuntimeException("Error reading CSV file: " + e.getMessage(), e);
    }
  }
}
