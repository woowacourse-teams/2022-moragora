import React, { createContext, useRef, useState } from 'react';
import { MeetingEvent } from 'types/eventType';
import { mergeArrays } from 'utils/common';
import {
  dateToFormattedString,
  getAllDatesInMonth,
  getAllSameDays,
  getlastDateOfMonth,
} from 'utils/timeUtil';

export const CalendarContext = createContext<{
  initialDate?: Date;
  currentDate?: Date;
  dates: Date[];
  selectedDates: Date[];
  events: MeetingEvent[];
  shouldApplyBeginEndDates: boolean;
  selectDate: (newDate: Date) => void;
  unselectDate: (newDate: Date) => void;
  navigateMonthTo: (amount: number) => void;
  clearSelectedDates: () => void;
  selectDay: (day: number) => void;
  bindDateCellControlRef: (date: Date) => React.RefCallback<HTMLDivElement>;
  clearDateCellControlRef: () => void;
  highlightDateCellsHaveSameEntranceAndLeaveTime: (date: Date) => void;
  removeHighlightFromDateCells: () => void;
  addEvents: (events: MeetingEvent[]) => void;
  removeEvents: (dates: Date[]) => void;
  setShouldApplyBeginEndDates: (flag: boolean) => void;
  setBeginDate: (date: Date) => void;
  setEndDate: (date: Date) => void;
}>({
  selectedDates: [],
  dates: [],
  events: [],
  shouldApplyBeginEndDates: false,
  selectDate: () => {},
  unselectDate: () => {},
  navigateMonthTo: () => {},
  clearSelectedDates: () => {},
  selectDay: () => {},
  bindDateCellControlRef: () => () => {},
  clearDateCellControlRef: () => {},
  highlightDateCellsHaveSameEntranceAndLeaveTime: () => {},
  removeHighlightFromDateCells: () => {},
  addEvents: () => {},
  removeEvents: () => {},
  setShouldApplyBeginEndDates: () => {},
  setBeginDate: () => {},
  setEndDate: () => {},
});

export const CalendarProvider: React.FC<
  React.PropsWithChildren<{ initialDate: Date }>
> = ({ initialDate, children }) => {
  const [currentDate, setCurrentDate] = useState(new Date(initialDate));
  const [dates, setDates] = useState(
    getAllDatesInMonth(initialDate.getFullYear(), initialDate.getMonth())
  );
  const [selectedDates, setSelectedDates] = useState<Date[]>([]);
  const [shouldApplyBeginEndDates, setShouldApplyBeginEndDates] =
    useState(true);
  const [beginDate, setBeginDate] = useState<Date>();
  const [endDate, setEndDate] = useState<Date>();
  const [events, setEvents] = useState<MeetingEvent[]>([]);
  const dateCellControlRef = useRef<{ element: HTMLDivElement; date: Date }[]>(
    []
  );
  const clearDateCellControlRef = () => {
    dateCellControlRef.current = [];
  };

  const bindDateCellControlRef =
    (date: Date): React.RefCallback<HTMLDivElement> =>
    (element) => {
      if (element) {
        dateCellControlRef.current.push({ element, date });
      }
    };

  const highlightDateCellsHaveSameEntranceAndLeaveTime = (
    hoveredDate: Date
  ) => {
    dateCellControlRef.current.forEach(({ element, date }) => {
      const hoveredEvent = events.find(
        (event) => event.date === dateToFormattedString(hoveredDate)
      );
      const currentEvent = events.find(
        (event) => event.date === dateToFormattedString(date)
      );
      const shouldHighlight =
        hoveredEvent &&
        currentEvent &&
        hoveredEvent.entranceTime === currentEvent.entranceTime &&
        hoveredEvent.leaveTime === currentEvent.leaveTime;

      if (shouldHighlight) {
        element.classList.add('highlight');
      }
    });
  };

  const removeHighlightFromDateCells = () => {
    dateCellControlRef.current.forEach(({ element }) => {
      element.classList.remove('highlight');
    });
  };

  const navigateMonthTo = (amount: number) => {
    const targetMonth = currentDate;

    targetMonth.setMonth(targetMonth.getMonth() + amount);

    setCurrentDate(targetMonth);
    setDates(
      getAllDatesInMonth(targetMonth.getFullYear(), targetMonth.getMonth())
    );
  };

  const selectDate = (newDate: Date) => {
    setSelectedDates((prev) => {
      if (
        prev.find(
          (prevDate) =>
            dateToFormattedString(prevDate) === dateToFormattedString(newDate)
        )
      ) {
        return prev;
      }

      return [...prev, newDate];
    });
  };

  const unselectDate = (newDate: Date) => {
    setSelectedDates((prev) =>
      prev.filter(
        (prevDate) =>
          dateToFormattedString(prevDate) !== dateToFormattedString(newDate)
      )
    );
  };

  const clearSelectedDates = () => {
    setSelectedDates([]);
  };

  const selectDay = (day: number) => {
    const isCurrentMonth =
      initialDate.getFullYear() === currentDate.getFullYear() &&
      initialDate.getMonth() === currentDate.getMonth();
    const leastAvailableDateOfCurrentMonth = new Date(
      currentDate.getFullYear(),
      currentDate.getMonth(),
      isCurrentMonth ? initialDate.getDate() : 1
    );

    const lastDateOfCurrentMonth = getlastDateOfMonth(currentDate);

    const begin =
      shouldApplyBeginEndDates && beginDate
        ? beginDate
        : leastAvailableDateOfCurrentMonth;
    const end =
      shouldApplyBeginEndDates && endDate ? endDate : lastDateOfCurrentMonth;
    const minBegin =
      begin.getTime() > initialDate.getTime() ? begin : initialDate;
    const minEnd = end.getTime() > initialDate.getTime() ? end : initialDate;

    getAllSameDays(day, minBegin, minEnd).forEach((date) => {
      selectDate(date);
    });
  };

  const addEvents = (newEvents: MeetingEvent[]) => {
    setEvents((prevEvents) => mergeArrays(prevEvents, newEvents, 'date'));
  };

  const removeEvents = (dates: Date[]) => {
    setEvents((prev) =>
      prev.filter(
        (event) =>
          !dates.find((date) => dateToFormattedString(date) === event.date)
      )
    );
  };

  const initialValue = {
    initialDate,
    currentDate,
    dates,
    selectedDates,
    events,
    shouldApplyBeginEndDates,
    navigateMonthTo,
    selectDate,
    unselectDate,
    clearSelectedDates,
    selectDay,
    bindDateCellControlRef,
    clearDateCellControlRef,
    highlightDateCellsHaveSameEntranceAndLeaveTime,
    removeHighlightFromDateCells,
    addEvents,
    removeEvents,
    setShouldApplyBeginEndDates,
    setBeginDate,
    setEndDate,
  };

  return (
    <CalendarContext.Provider value={initialValue}>
      {children}
    </CalendarContext.Provider>
  );
};
