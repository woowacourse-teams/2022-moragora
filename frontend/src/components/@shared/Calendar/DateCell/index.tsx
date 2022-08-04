import React, { forwardRef, useContext } from 'react';
import { CalendarContext } from 'contexts/calendarContext';
import * as S from './DateCell.styled';
import { dateToFormattedString } from 'utils/timeUtil';
import { MeetingEvent } from 'types/eventType';

type DateCellProps = {
  date?: Date;
  event?: MeetingEvent;
  disabled?: boolean;
} & React.HTMLAttributes<HTMLDivElement>;

const DateCell = forwardRef<HTMLDivElement, DateCellProps>(
  ({ date, event, disabled, ...props }, ref) => {
    const {
      initialDate,
      selectedDates,
      selectDate,
      unselectDate,
      highlightDateCellsHaveSameEntranceAndLeaveTime,
      removeHighlightFromDateCells,
    } = useContext(CalendarContext);

    if (!date) {
      return <S.Layout />;
    }

    const isSelected = !!selectedDates.find(
      (selectedDate) =>
        dateToFormattedString(selectedDate) === dateToFormattedString(date)
    );

    const isPreviousDate =
      initialDate && date.getTime() < initialDate?.getTime();

    const handleClick: React.MouseEventHandler<HTMLDivElement> = () => {
      isSelected ? unselectDate(date) : selectDate(date);
    };

    const handleMouseEnter: React.MouseEventHandler<HTMLDivElement> = () => {
      highlightDateCellsHaveSameEntranceAndLeaveTime(date);
    };

    const handleMouseLeave: React.MouseEventHandler<HTMLDivElement> = () => {
      removeHighlightFromDateCells();
    };

    return (
      <S.Layout
        {...props}
        ref={ref}
        isSelected={isSelected}
        event={event}
        disabled={isPreviousDate || disabled}
        onClick={handleClick}
        onMouseEnter={handleMouseEnter}
        onMouseLeave={handleMouseLeave}
        className="noselect"
      >
        {date?.getDate()}
      </S.Layout>
    );
  }
);

export default DateCell;
