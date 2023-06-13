package com.campusdual.jardhotelsontimize.model.core.service;

import com.campusdual.jardhotelsontimize.model.core.dao.BookingDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    DefaultOntimizeDaoHelper daoHelper;

    @InjectMocks
    BookingService bookingService;

    @Mock
    RoomService roomService;

    @Mock
    BookingDao bookingDao;


    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class BookingServiceQuery {
        @Test
        void bookingQueryTest() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(0);
            er.put("id", 1);
            Map<String, Object> bookingToQuery = new HashMap<>();
            bookingToQuery.put("id", 1);
            when(daoHelper.query(any(BookingDao.class), anyMap(), anyList())).thenReturn(er);
            EntityResult result = bookingService.bookingQuery(bookingToQuery, new ArrayList<>());
            assertEquals(0, result.getCode());
            assertEquals("", result.getMessage());
            verify(daoHelper, times(1)).query(any(BookingDao.class), anyMap(), anyList());
        }

        @Test
        void bookingQueryTestNotFound() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(1);
            Map<String, Object> bookingToQuery = new HashMap<>();
            bookingToQuery.put("id", 1);
            when(daoHelper.query(any(BookingDao.class), anyMap(), anyList())).thenReturn(er);
            EntityResult result = bookingService.bookingQuery(bookingToQuery, new ArrayList<>());
            assertEquals(1, result.getCode());
            assertEquals("The booking doesn't exist", result.getMessage());
            verify(daoHelper, times(1)).query(any(BookingDao.class), anyMap(), anyList());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class BookingServiceInsert {
        @Test
        void bookingInsertTest() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(0);
            er.put("id", 1);
            Map<String, Object> bookingToInsert = new HashMap<>();
            bookingToInsert.put("id", 1);
            bookingToInsert.put("room", 1);
            EntityResult res = new EntityResultMapImpl();
            res.put("price", 100);
            when(roomService.roomQuery(any(), anyList())).thenReturn(res);
            when(daoHelper.insert(any(BookingDao.class), anyMap())).thenReturn(er);
            EntityResult result = bookingService.bookingInsert(bookingToInsert);
            assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).insert(any(BookingDao.class), anyMap());
            assertEquals("Successful booking insertion", result.getMessage());
        }

        @Test
        void bookingInsertTestFail() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(1);
            er.put("id", 1);
            Map<String, Object> bookingToInsert = new HashMap<>();
            bookingToInsert.put("id", 1);
            bookingToInsert.put("room", 1);
            EntityResult res = new EntityResultMapImpl();
            res.put("price", 100);
            when(roomService.roomQuery(any(), anyList())).thenReturn(res);
            when(daoHelper.insert(any(BookingDao.class), anyMap())).thenThrow(new RuntimeException(""));
            EntityResult result = bookingService.bookingInsert(bookingToInsert);
            assertEquals(1, result.getCode());
            verify(daoHelper, times(1)).insert(any(BookingDao.class), anyMap());
            assertNotEquals("Successful booking insertion", result.getMessage());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class BookingServiceUpdate {
        @Test
        void bookingUpdateTest() {
            EntityResult er = new EntityResultMapImpl();
            EntityResult er2 = new EntityResultMapImpl();
            er.setCode(0);
            er.put("id", 1);
            er.put("totalprice", 650);
            er2.put("price", List.of(BigDecimal.valueOf(650)));
            Map<String, Object> bookingToUpdate = new HashMap<>();
            bookingToUpdate.put("room", 1);
            bookingToUpdate.put("arrivaldate", "2023-06-03");
            bookingToUpdate.put("departuredate", "2023-06-06");
            Map<String, Object> bookingKey = new HashMap<>();
            bookingKey.put("id", 1);
            when(daoHelper.update(any(BookingDao.class), anyMap(), anyMap())).thenReturn(er);
            when(roomService.roomQuery(anyMap(), anyList())).thenReturn(er2);
            EntityResult result = bookingService.bookingUpdate(bookingToUpdate, bookingKey);
            assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).update(any(BookingDao.class), anyMap(), anyMap());
            assertEquals("Successful booking update", result.getMessage());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class BookingServiceDelete {
        @Test
        void bookingDeleteTest() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(0);
            er.setMessage("");
            er.put("id", 1);
            er.put("totalprice", List.of(new BigDecimal("650.5")));
            er.put("arrivaldate", List.of("2025-06-06"));
            Map<String, Object> bookingKey = new HashMap<>();
            bookingKey.put("id", 1);
            when(daoHelper.delete(any(BookingDao.class), anyMap())).thenReturn(er);
            when(daoHelper.query(any(BookingDao.class), anyMap(), anyList())).thenReturn(er);
            EntityResult result = bookingService.bookingDelete(bookingKey);
            assertEquals(0, result.getCode());
            verify(daoHelper, times(1)).delete(any(BookingDao.class), anyMap());
            assertEquals("Successful booking delete", result.getMessage());
        }

        @Test
        void bookingDeleteTestBookingNotFound() {
            EntityResult er = new EntityResultMapImpl();
            er.setCode(1);
            er.setMessage("");
            Map<String, Object> bookingKey = new HashMap<>();
            bookingKey.put("id", 1);
            when(daoHelper.delete(any(BookingDao.class), anyMap())).thenReturn(er);
            when(daoHelper.query(any(BookingDao.class), anyMap(), anyList())).thenReturn(er);
            EntityResult result = bookingService.bookingDelete(bookingKey);
            assertEquals(1, result.getCode());
            verify(daoHelper, times(1)).delete(any(BookingDao.class), anyMap());
            assertEquals("Booking not found", result.getMessage());
        }
    }


}
