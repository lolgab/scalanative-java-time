package org.scalajs.testsuite.javalib.time

import java.time._
import java.time.temporal._

import utest._

object LocalTimeTestTemporal extends TestSuite with TemporalTest[LocalTime] {

  import DateTimeTestUtil._
  import LocalTime._
  import ChronoUnit._
  import ChronoField._

  val samples = Seq(MAX, NOON, MIN)

  def isSupported(unit: ChronoUnit): Boolean = unit.isTimeBased

  def isSupported(field: ChronoField): Boolean = field.isTimeBased

  val tests = temporalTests
}

object LocalTimeTest extends TestSuite {

  import DateTimeTestUtil._
  import LocalTime._
  import ChronoUnit._
  import ChronoField._

  val samples = Seq(MAX, NOON, MIN)

  val tests = Tests {

    'test_getLong - {
      assert(0L == MIN.getLong(NANO_OF_SECOND))
      assert(0L == MIN.getLong(NANO_OF_DAY))
      assert(0L == MIN.getLong(MICRO_OF_SECOND))
      assert(0L == MIN.getLong(MICRO_OF_DAY))
      assert(0L == MIN.getLong(MILLI_OF_SECOND))
      assert(0L == MIN.getLong(MILLI_OF_DAY))
      assert(0L == MIN.getLong(SECOND_OF_MINUTE))
      assert(0L == MIN.getLong(SECOND_OF_DAY))
      assert(0L == MIN.getLong(MINUTE_OF_HOUR))
      assert(0L == MIN.getLong(MINUTE_OF_DAY))
      assert(0L == MIN.getLong(HOUR_OF_AMPM))
      assert(12L == MIN.getLong(CLOCK_HOUR_OF_AMPM))
      assert(0L == MIN.getLong(HOUR_OF_DAY))
      assert(24L == MIN.getLong(CLOCK_HOUR_OF_DAY))
      assert(0L == MIN.getLong(AMPM_OF_DAY))

      assert(999999999L == MAX.getLong(NANO_OF_SECOND))
      assert(86399999999999L == MAX.getLong(NANO_OF_DAY))
      assert(999999L == MAX.getLong(MICRO_OF_SECOND))
      assert(86399999999L == MAX.getLong(MICRO_OF_DAY))
      assert(999L == MAX.getLong(MILLI_OF_SECOND))
      assert(86399999L == MAX.getLong(MILLI_OF_DAY))
      assert(59L == MAX.getLong(SECOND_OF_MINUTE))
      assert(86399L == MAX.getLong(SECOND_OF_DAY))
      assert(59L == MAX.getLong(MINUTE_OF_HOUR))
      assert(1439L == MAX.getLong(MINUTE_OF_DAY))
      assert(11L == MAX.getLong(HOUR_OF_AMPM))
      assert(11L == MAX.getLong(CLOCK_HOUR_OF_AMPM))
      assert(23L == MAX.getLong(HOUR_OF_DAY))
      assert(23L == MAX.getLong(CLOCK_HOUR_OF_DAY))
      assert(1L == MAX.getLong(AMPM_OF_DAY))
    }

    'test_getHour - {
      assert(0 == MIN.getHour)
      assert(12 == NOON.getHour)
      assert(23 == MAX.getHour)
    }

    'test_getMinute - {
      assert(0 == MIN.getMinute)
      assert(30 == of(0, 30).getMinute)
      assert(59 == MAX.getMinute)
    }

    'test_getSecond - {
      assert(0 == MIN.getSecond)
      assert(30 == of(0, 0, 30).getSecond)
      assert(59 == MAX.getSecond)
    }

    'test_getNano - {
      assert(0 == MIN.getNano)
      assert(999999999 == MAX.getNano)
    }

    'test_with - {
      for (t <- samples) {
        for (n <- Seq(0, 999, 999999, 999999999))
          testDateTime(t.`with`(NANO_OF_SECOND, n))(t.withNano(n))
        for (n <- Seq(0L, 1000000000L, 86399999999999L))
          testDateTime(t.`with`(NANO_OF_DAY, n))(ofNanoOfDay(n))
        for (n <- Seq(0, 999, 999999))
          testDateTime(t.`with`(MICRO_OF_SECOND, n))(t.withNano(n * 1000))
        for (n <- Seq(0L, 1000000L, 86399999999L))
          testDateTime(t.`with`(MICRO_OF_DAY, n))(ofNanoOfDay(n * 1000))
        for (n <- Seq(0, 500, 999))
          testDateTime(t.`with`(MILLI_OF_SECOND, n))(t.withNano(n * 1000000))
        for (n <- Seq(0L, 1000L, 86399999L))
          testDateTime(t.`with`(MILLI_OF_DAY, n))(ofNanoOfDay(n * 1000000))
        for (n <- Seq(0, 30, 59))
          testDateTime(t.`with`(SECOND_OF_MINUTE, n))(t.withSecond(n))
        for (n <- Seq(0, 60, 86399))
          testDateTime(t.`with`(SECOND_OF_DAY, n))(
            ofSecondOfDay(n).withNano(t.getNano))
        for (n <- Seq(0, 30, 59))
          testDateTime(t.`with`(MINUTE_OF_HOUR, n))(t.withMinute(n))
        for (n <- Seq(0, 60, 1439)) {
          testDateTime(t.`with`(MINUTE_OF_DAY, n)) {
            ofSecondOfDay(n * 60).withSecond(t.getSecond).withNano(t.getNano)
          }
        }
        for (n <- Seq(0, 6, 11)) {
          val h = (t.getHour / 12) * 12 + n
          testDateTime(t.`with`(HOUR_OF_AMPM, n))(t.withHour(h))
        }
        for (n <- Seq(1, 6, 12)) {
          val h = (t.getHour / 12) * 12 + (n % 12)
          testDateTime(t.`with`(CLOCK_HOUR_OF_AMPM, n))(t.withHour(h))
        }
        for (n <- Seq(0, 12, 23))
          testDateTime(t.`with`(HOUR_OF_DAY, n))(t.withHour(n))
        for (n <- Seq(1, 12, 24))
          testDateTime(t.`with`(CLOCK_HOUR_OF_DAY, n))(t.withHour(n % 24))
        for (n <- Seq(0, 1)) {
          val h = t.getHour % 12 + n * 12
          testDateTime(t.`with`(AMPM_OF_DAY, n))(t.withHour(h))
        }

        for (n <- Seq(Long.MinValue, -1L, 1000000000L, Long.MaxValue))
          intercept[DateTimeException](t.`with`(NANO_OF_SECOND, n))
        for (n <- Seq(Long.MinValue, -1L, 86400000000000L, Long.MaxValue))
          intercept[DateTimeException](t.`with`(NANO_OF_DAY, n))
        for (n <- Seq(Long.MinValue, -1L, 1000000L, Long.MaxValue))
          intercept[DateTimeException](t.`with`(MICRO_OF_SECOND, n))
        for (n <- Seq(Long.MinValue, -1L, 86400000000L, Long.MaxValue))
          intercept[DateTimeException](t.`with`(MICRO_OF_DAY, n))
        for (n <- Seq(Long.MinValue, -1L, 1000L, Long.MaxValue))
          intercept[DateTimeException](t.`with`(MILLI_OF_SECOND, n))
        for (n <- Seq(Long.MinValue, -1L, 86400000L, Long.MaxValue))
          intercept[DateTimeException](t.`with`(MILLI_OF_DAY, n))
        for (n <- Seq(Long.MinValue, -1L, 60L, Long.MaxValue))
          intercept[DateTimeException](t.`with`(SECOND_OF_MINUTE, n))
        for (n <- Seq(Long.MinValue, -1L, 86400L, Long.MaxValue))
          intercept[DateTimeException](t.`with`(SECOND_OF_DAY, n))
        for (n <- Seq(Long.MinValue, -1L, 60L, Long.MaxValue))
          intercept[DateTimeException](t.`with`(MINUTE_OF_HOUR, n))
        for (n <- Seq(Long.MinValue, -1L, 1440L, Long.MaxValue))
          intercept[DateTimeException](t.`with`(MINUTE_OF_DAY, n))
        for (n <- Seq(Long.MinValue, -1L, 12L, Long.MaxValue))
          intercept[DateTimeException](t.`with`(HOUR_OF_AMPM, n))
        for (n <- Seq(Long.MinValue, 0L, 13L, Long.MaxValue))
          intercept[DateTimeException](t.`with`(CLOCK_HOUR_OF_AMPM, n))
        for (n <- Seq(Long.MinValue, -1L, 24L, Long.MaxValue))
          intercept[DateTimeException](t.`with`(HOUR_OF_DAY, n))
        for (n <- Seq(Long.MinValue, 0L, 25L, Long.MaxValue))
          intercept[DateTimeException](t.`with`(CLOCK_HOUR_OF_DAY, n))
        for (n <- Seq(Long.MinValue, -1L, 2L, Long.MaxValue))
          intercept[DateTimeException](t.`with`(AMPM_OF_DAY, n))
      }
    }

    'test_withHour - {
      testDateTime(MIN.withHour(0))(MIN)
      testDateTime(MIN.withHour(12))(NOON)
      testDateTime(MIN.withHour(23))(of(23, 0))
      testDateTime(MAX.withHour(0))(of(0, 59, 59, 999999999))
      testDateTime(MAX.withHour(23))(MAX)

      for (t <- samples) {
        intercept[DateTimeException](t.withHour(Int.MinValue))
        intercept[DateTimeException](t.withHour(-1))
        intercept[DateTimeException](t.withHour(24))
        intercept[DateTimeException](t.withHour(Int.MaxValue))
      }
    }

    'test_withMinute - {
      testDateTime(MIN.withMinute(0))(MIN)
      testDateTime(MIN.withMinute(30))(of(0, 30))
      testDateTime(MIN.withMinute(59))(of(0, 59))
      testDateTime(MAX.withMinute(0))(of(23, 0, 59, 999999999))
      testDateTime(MAX.withMinute(59))(MAX)

      for (t <- samples) {
        intercept[DateTimeException](t.withMinute(Int.MinValue))
        intercept[DateTimeException](t.withMinute(-1))
        intercept[DateTimeException](t.withMinute(60))
        intercept[DateTimeException](t.withMinute(Int.MaxValue))
      }
    }

    'test_withSecond - {
      testDateTime(MIN.withSecond(0))(MIN)
      testDateTime(MIN.withSecond(30))(of(0, 0, 30))
      testDateTime(MIN.withSecond(59))(of(0, 0, 59))
      testDateTime(MAX.withSecond(0))(of(23, 59, 0, 999999999))
      testDateTime(MAX.withSecond(59))(MAX)

      for (t <- samples) {
        intercept[DateTimeException](t.withSecond(Int.MinValue))
        intercept[DateTimeException](t.withSecond(-1))
        intercept[DateTimeException](t.withSecond(60))
        intercept[DateTimeException](t.withSecond(Int.MaxValue))
      }
    }

    'test_withNano - {
      testDateTime(MIN.withNano(0))(MIN)
      testDateTime(MIN.withNano(500000000))(of(0, 0, 0, 500000000))
      testDateTime(MIN.withNano(999999999))(of(0, 0, 0, 999999999))
      testDateTime(MAX.withNano(0))(of(23, 59, 59, 0))
      testDateTime(MAX.withNano(999999999))(MAX)

      for (t <- samples) {
        intercept[DateTimeException](t.withNano(Int.MinValue))
        intercept[DateTimeException](t.withNano(-1))
        intercept[DateTimeException](t.withNano(1000000000))
        intercept[DateTimeException](t.withNano(Int.MaxValue))
      }
    }

    'test_truncatedTo - {
      testDateTime(MIN.truncatedTo(NANOS))(MIN)
      testDateTime(MAX.truncatedTo(NANOS))(MAX)
      testDateTime(MIN.truncatedTo(MICROS))(MIN)
      testDateTime(MAX.truncatedTo(MICROS))(of(23, 59, 59, 999999000))
      testDateTime(MIN.truncatedTo(MILLIS))(MIN)
      testDateTime(MAX.truncatedTo(MILLIS))(of(23, 59, 59, 999000000))
      testDateTime(MIN.truncatedTo(SECONDS))(MIN)
      testDateTime(MAX.truncatedTo(SECONDS))(of(23, 59, 59))
      testDateTime(MIN.truncatedTo(MINUTES))(MIN)
      testDateTime(MAX.truncatedTo(MINUTES))(of(23, 59))
      testDateTime(MIN.truncatedTo(HOURS))(MIN)
      testDateTime(MAX.truncatedTo(HOURS))(of(23, 0))
      testDateTime(MIN.truncatedTo(HALF_DAYS))(MIN)
      testDateTime(MAX.truncatedTo(HALF_DAYS))(of(12, 0))
      testDateTime(MIN.truncatedTo(DAYS))(MIN)
      testDateTime(MAX.truncatedTo(DAYS))(MIN)

      val illegalUnits = dateBasedUnits.filter(_ != DAYS)
      for {
        t <- samples
        u <- illegalUnits
      } {
        intercept[UnsupportedTemporalTypeException](t.truncatedTo(u))
      }
    }

    'test_plus - {
      val values = Seq(Long.MinValue,
                       -1000000000L,
                       -86400L,
                       -3600L,
                       -60L,
                       -1L,
                       0L,
                       1L,
                       60L,
                       3600L,
                       86400L,
                       1000000000L,
                       Long.MaxValue)

      for {
        t <- samples
        n <- values
      } {
        testDateTime(t.plus(n, NANOS))(t.plusNanos(n))
        testDateTime(t.plus(n, MICROS))(t.plusNanos((n % 86400000000L) * 1000))
        testDateTime(t.plus(n, MILLIS))(t.plusNanos((n % 86400000) * 1000000))
        testDateTime(t.plus(n, SECONDS))(t.plusSeconds(n))
        testDateTime(t.plus(n, MINUTES))(t.plusMinutes(n))
        testDateTime(t.plus(n, HOURS))(t.plusHours(n))
        testDateTime(t.plus(n, HALF_DAYS))(t.plusHours((n % 2) * 12))
      }
    }

    'test_plusHours - {
      testDateTime(MIN.plusHours(Long.MinValue))(of(16, 0))
      testDateTime(MIN.plusHours(-24))(MIN)
      testDateTime(MIN.plusHours(-1))(of(23, 0))
      testDateTime(MIN.plusHours(0))(MIN)
      testDateTime(MIN.plusHours(1))(of(1, 0))
      testDateTime(MIN.plusHours(24))(MIN)
      testDateTime(MIN.plusHours(Long.MaxValue))(of(7, 0))
      testDateTime(MAX.plusHours(Long.MinValue))(of(15, 59, 59, 999999999))
      testDateTime(MAX.plusHours(-24))(MAX)
      testDateTime(MAX.plusHours(-1))(of(22, 59, 59, 999999999))
      testDateTime(MAX.plusHours(0))(MAX)
      testDateTime(MAX.plusHours(1))(of(0, 59, 59, 999999999))
      testDateTime(MAX.plusHours(24))(MAX)
      testDateTime(MAX.plusHours(Long.MaxValue))(of(6, 59, 59, 999999999))
    }

    'test_plusMinutes - {
      testDateTime(MIN.plusMinutes(Long.MinValue))(of(5, 52))
      testDateTime(MIN.plusMinutes(-1440))(MIN)
      testDateTime(MIN.plusMinutes(-60))(of(23, 0))
      testDateTime(MIN.plusMinutes(-1))(of(23, 59))
      testDateTime(MIN.plusMinutes(0))(MIN)
      testDateTime(MIN.plusMinutes(1))(of(0, 1))
      testDateTime(MIN.plusMinutes(60))(of(1, 0))
      testDateTime(MIN.plusMinutes(1440))(MIN)
      testDateTime(MIN.plusMinutes(Long.MaxValue))(of(18, 7))
      testDateTime(MAX.plusMinutes(Long.MinValue))(of(5, 51, 59, 999999999))
      testDateTime(MAX.plusMinutes(-1440))(MAX)
      testDateTime(MAX.plusMinutes(-60))(of(22, 59, 59, 999999999))
      testDateTime(MAX.plusMinutes(-1))(of(23, 58, 59, 999999999))
      testDateTime(MAX.plusMinutes(0))(MAX)
      testDateTime(MAX.plusMinutes(1))(of(0, 0, 59, 999999999))
      testDateTime(MAX.plusMinutes(60))(of(0, 59, 59, 999999999))
      testDateTime(MAX.plusMinutes(1440))(MAX)
      testDateTime(MAX.plusMinutes(Long.MaxValue))(of(18, 6, 59, 999999999))
    }

    'test_plusSeconds - {
      testDateTime(MIN.plusSeconds(Long.MinValue))(of(8, 29, 52))
      testDateTime(MIN.plusSeconds(-86400))(MIN)
      testDateTime(MIN.plusSeconds(-60))(of(23, 59))
      testDateTime(MIN.plusSeconds(-1))(of(23, 59, 59))
      testDateTime(MIN.plusSeconds(0))(MIN)
      testDateTime(MIN.plusSeconds(1))(of(0, 0, 1))
      testDateTime(MIN.plusSeconds(60))(of(0, 1))
      testDateTime(MIN.plusSeconds(86400))(MIN)
      testDateTime(MIN.plusSeconds(Long.MaxValue))(of(15, 30, 7))
      testDateTime(MAX.plusSeconds(Long.MinValue))(of(8, 29, 51, 999999999))
      testDateTime(MAX.plusSeconds(-86400))(MAX)
      testDateTime(MAX.plusSeconds(-60))(of(23, 58, 59, 999999999))
      testDateTime(MAX.plusSeconds(-1))(of(23, 59, 58, 999999999))
      testDateTime(MAX.plusSeconds(0))(MAX)
      testDateTime(MAX.plusSeconds(1))(of(0, 0, 0, 999999999))
      testDateTime(MAX.plusSeconds(60))(of(0, 0, 59, 999999999))
      testDateTime(MAX.plusSeconds(86400))(MAX)
      testDateTime(MAX.plusSeconds(Long.MaxValue))(of(15, 30, 6, 999999999))
    }

    'test_plusNanos - {
      testDateTime(MIN.plusNanos(Long.MinValue))(of(0, 12, 43, 145224192))
      testDateTime(MIN.plusNanos(-86400000000000L))(MIN)
      testDateTime(MIN.plusNanos(-1000000000))(of(23, 59, 59))
      testDateTime(MIN.plusNanos(-1))(MAX)
      testDateTime(MIN.plusNanos(0))(MIN)
      testDateTime(MIN.plusNanos(1))(of(0, 0, 0, 1))
      testDateTime(MIN.plusNanos(1000000000))(of(0, 0, 1))
      testDateTime(MIN.plusNanos(86400000000000L))(MIN)
      testDateTime(MIN.plusNanos(Long.MaxValue))(of(23, 47, 16, 854775807))
      testDateTime(MAX.plusNanos(Long.MinValue))(of(0, 12, 43, 145224191))
      testDateTime(MAX.plusNanos(-86400000000000L))(MAX)
      testDateTime(MAX.plusNanos(-1000000000))(of(23, 59, 58, 999999999))
      testDateTime(MAX.plusNanos(-1))(of(23, 59, 59, 999999998))
      testDateTime(MAX.plusNanos(0))(MAX)
      testDateTime(MAX.plusNanos(1))(MIN)
      testDateTime(MAX.plusNanos(1000000000))(of(0, 0, 0, 999999999))
      testDateTime(MAX.plusNanos(86400000000000L))(MAX)
      testDateTime(MAX.plusNanos(Long.MaxValue))(of(23, 47, 16, 854775806))
    }

    'test_minusHours - {
      testDateTime(MIN.minusHours(Long.MinValue))(of(8, 0))
      testDateTime(MIN.minusHours(-24))(MIN)
      testDateTime(MIN.minusHours(-1))(of(1, 0))
      testDateTime(MIN.minusHours(0))(MIN)
      testDateTime(MIN.minusHours(1))(of(23, 0))
      testDateTime(MIN.minusHours(24))(MIN)
      testDateTime(MIN.minusHours(Long.MaxValue))(of(17, 0))
      testDateTime(MAX.minusHours(Long.MinValue))(of(7, 59, 59, 999999999))
      testDateTime(MAX.minusHours(-24))(MAX)
      testDateTime(MAX.minusHours(-1))(of(0, 59, 59, 999999999))
      testDateTime(MAX.minusHours(0))(MAX)
      testDateTime(MAX.minusHours(1))(of(22, 59, 59, 999999999))
      testDateTime(MAX.minusHours(24))(MAX)
      testDateTime(MAX.minusHours(Long.MaxValue))(of(16, 59, 59, 999999999))
    }

    'test_minusMinutes - {
      testDateTime(MIN.minusMinutes(Long.MinValue))(of(18, 8))
      testDateTime(MIN.minusMinutes(-1440))(MIN)
      testDateTime(MIN.minusMinutes(-60))(of(1, 0))
      testDateTime(MIN.minusMinutes(-1))(of(0, 1))
      testDateTime(MIN.minusMinutes(0))(MIN)
      testDateTime(MIN.minusMinutes(1))(of(23, 59))
      testDateTime(MIN.minusMinutes(60))(of(23, 0))
      testDateTime(MIN.minusMinutes(1440))(MIN)
      testDateTime(MIN.minusMinutes(Long.MaxValue))(of(5, 53))
      testDateTime(MAX.minusMinutes(Long.MinValue))(of(18, 7, 59, 999999999))
      testDateTime(MAX.minusMinutes(-1440))(MAX)
      testDateTime(MAX.minusMinutes(-60))(of(0, 59, 59, 999999999))
      testDateTime(MAX.minusMinutes(-1))(of(0, 0, 59, 999999999))
      testDateTime(MAX.minusMinutes(0))(MAX)
      testDateTime(MAX.minusMinutes(1))(of(23, 58, 59, 999999999))
      testDateTime(MAX.minusMinutes(60))(of(22, 59, 59, 999999999))
      testDateTime(MAX.minusMinutes(1440))(MAX)
      testDateTime(MAX.minusMinutes(Long.MaxValue))(of(5, 52, 59, 999999999))
    }

    'test_minusSeconds - {
      testDateTime(MIN.minusSeconds(Long.MinValue))(of(15, 30, 8))
      testDateTime(MIN.minusSeconds(-86400))(MIN)
      testDateTime(MIN.minusSeconds(-60))(of(0, 1))
      testDateTime(MIN.minusSeconds(-1))(of(0, 0, 1))
      testDateTime(MIN.minusSeconds(0))(MIN)
      testDateTime(MIN.minusSeconds(1))(of(23, 59, 59))
      testDateTime(MIN.minusSeconds(60))(of(23, 59))
      testDateTime(MIN.minusSeconds(86400))(MIN)
      testDateTime(MIN.minusSeconds(Long.MaxValue))(of(8, 29, 53))
      testDateTime(MAX.minusSeconds(Long.MinValue))(of(15, 30, 7, 999999999))
      testDateTime(MAX.minusSeconds(-86400))(MAX)
      testDateTime(MAX.minusSeconds(-60))(of(0, 0, 59, 999999999))
      testDateTime(MAX.minusSeconds(-1))(of(0, 0, 0, 999999999))
      testDateTime(MAX.minusSeconds(0))(MAX)
      testDateTime(MAX.minusSeconds(1))(of(23, 59, 58, 999999999))
      testDateTime(MAX.minusSeconds(60))(of(23, 58, 59, 999999999))
      testDateTime(MAX.minusSeconds(86400))(MAX)
      testDateTime(MAX.minusSeconds(Long.MaxValue))(of(8, 29, 52, 999999999))
    }

    'test_minusNanos - {
      testDateTime(MIN.minusNanos(Long.MinValue))(of(23, 47, 16, 854775808))
      testDateTime(MIN.minusNanos(-86400000000000L))(MIN)
      testDateTime(MIN.minusNanos(-1000000000))(of(0, 0, 1))
      testDateTime(MIN.minusNanos(-1))(of(0, 0, 0, 1))
      testDateTime(MIN.minusNanos(0))(MIN)
      testDateTime(MIN.minusNanos(1))(MAX)
      testDateTime(MIN.minusNanos(1000000000))(of(23, 59, 59))
      testDateTime(MIN.minusNanos(86400000000000L))(MIN)
      testDateTime(MIN.minusNanos(Long.MaxValue))(of(0, 12, 43, 145224193))
      testDateTime(MAX.minusNanos(Long.MinValue))(of(23, 47, 16, 854775807))
      testDateTime(MAX.minusNanos(-86400000000000L))(MAX)
      testDateTime(MAX.minusNanos(-1000000000))(of(0, 0, 0, 999999999))
      testDateTime(MAX.minusNanos(-1))(MIN)
      testDateTime(MAX.minusNanos(0))(MAX)
      testDateTime(MAX.minusNanos(1))(of(23, 59, 59, 999999998))
      testDateTime(MAX.minusNanos(1000000000))(of(23, 59, 58, 999999999))
      testDateTime(MAX.minusNanos(86400000000000L))(MAX)
      testDateTime(MAX.minusNanos(Long.MaxValue))(of(0, 12, 43, 145224192))
    }

    'test_adjustInto - {
      for {
        t1 <- samples
        t2 <- samples
      } {
        testDateTime(t1.adjustInto(t2))(t1)
      }

      val ds = Seq(LocalDate.MIN, LocalDate.MAX)
      for {
        t <- samples
        d <- ds
      } {
        intercept[DateTimeException](t.adjustInto(d))
      }
    }

    'test_until - {
      assert(86399999999999L == MIN.until(MAX, NANOS))
      assert(86399999999L == MIN.until(MAX, MICROS))
      assert(86399999L == MIN.until(MAX, MILLIS))
      assert(86399L == MIN.until(MAX, SECONDS))
      assert(1439L == MIN.until(MAX, MINUTES))
      assert(23L == MIN.until(MAX, HOURS))
      assert(1L == MIN.until(MAX, HALF_DAYS))

      for (u <- timeBasedUnits) {
        assert(-MIN.until(MAX, u) == MAX.until(MIN, u))
        assert(0L == MIN.until(MIN, u))
        assert(0L == MAX.until(MAX, u))
      }
    }

    'test_toSecondOfDay - {
      assert(0 == MIN.toSecondOfDay)
      assert(86399 == MAX.toSecondOfDay)
    }

    'test_toNanoOfDay - {
      assert(0L == MIN.toNanoOfDay)
      assert(86399999999999L == MAX.toNanoOfDay)
    }

    'test_compareTo - {
      assert(0 == MIN.compareTo(MIN))
      assert(MIN.compareTo(MAX) < 0)
      assert(MAX.compareTo(MIN) > 0)
      assert(0 == MAX.compareTo(MAX))
    }

    'test_isAfter - {
      assert(MIN.isAfter(MIN) == false)
      assert(MIN.isAfter(MAX) == false)
      assert(MAX.isAfter(MIN))
      assert(MAX.isAfter(MAX) == false)
    }

    'test_isBefore - {
      assert(MIN.isBefore(MIN) == false)
      assert(MIN.isBefore(MAX))
      assert(MAX.isBefore(MIN) == false)
      assert(MAX.isBefore(MAX) == false)
    }

    'test_toString - {
      assert("00:00" == MIN.toString)
      assert("23:59:59.999999999" == MAX.toString)
      assert("01:01" == of(1, 1).toString)
      assert("01:01:01" == of(1, 1, 1).toString)
      assert("01:01:01.000000001" == of(1, 1, 1, 1).toString)
      assert("01:01:01.100" == of(1, 1, 1, 100000000).toString)
      assert("01:01:01.100100" == of(1, 1, 1, 100100000).toString)
      assert("01:01:01.100100100" == of(1, 1, 1, 100100100).toString)
    }

    // TODO: port properly to Scala Native
    // 'test_now - {
    //   assert(now() != null)
    // }

    'test_of - {
      testDateTime(of(0, 0))(MIN)
      testDateTime(of(0, 0, 0))(MIN)
      testDateTime(of(0, 0, 0, 0))(MIN)
      testDateTime(of(23, 59))(of(23, 59, 0, 0))
      testDateTime(of(23, 59, 59))(of(23, 59, 59, 0))
      testDateTime(of(23, 59, 59, 999999999))(MAX)

      intercept[DateTimeException](of(-1, 0))
      intercept[DateTimeException](of(0, -1))
      intercept[DateTimeException](of(0, 0, -1))
      intercept[DateTimeException](of(0, 0, 0, -1))
      intercept[DateTimeException](of(24, 0))
      intercept[DateTimeException](of(0, 60))
      intercept[DateTimeException](of(0, 0, 60))
      intercept[DateTimeException](of(0, 0, 0, 1000000000))
    }

    'test_ofSecondOfDay - {
      testDateTime(ofSecondOfDay(0))(MIN)
      testDateTime(ofSecondOfDay(1))(of(0, 0, 1))
      testDateTime(ofSecondOfDay(60))(of(0, 1))
      testDateTime(ofSecondOfDay(86399))(of(23, 59, 59))

      intercept[DateTimeException](ofSecondOfDay(-1))
      intercept[DateTimeException](ofSecondOfDay(86400))
    }

    'test_ofNanoOfDay - {
      testDateTime(ofNanoOfDay(0))(MIN)
      testDateTime(ofNanoOfDay(1))(of(0, 0, 0, 1))
      testDateTime(ofNanoOfDay(1000000000))(of(0, 0, 1))
      testDateTime(ofNanoOfDay(86399999999999L))(MAX)

      intercept[DateTimeException](ofNanoOfDay(-1))
      intercept[DateTimeException](ofNanoOfDay(86400000000000L))
    }

    'test_from - {
      for (t <- samples)
        testDateTime(from(t))(t)

      intercept[DateTimeException](from(LocalDate.of(2012, 2, 29)))
      intercept[DateTimeException](from(Month.JANUARY))
      intercept[DateTimeException](from(DayOfWeek.MONDAY))
    }
  }
}
