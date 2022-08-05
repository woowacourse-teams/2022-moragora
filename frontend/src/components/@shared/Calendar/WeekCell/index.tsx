import React, { useContext } from 'react';
import { CalendarContext } from 'contexts/calendarContext';
import * as S from './WeekCell.styled';

type WeekCellProps = {
  day: number;
} & Omit<React.HTMLAttributes<HTMLButtonElement>, 'type' | 'onClick'>;

const WeekCell: React.FC<WeekCellProps> = ({ day, children, ...props }) => {
  const { selectDay } = useContext(CalendarContext);

  return (
    <S.Button
      type="button"
      onClick={() => {
        selectDay(day);
      }}
      {...props}
    >
      {children}
    </S.Button>
  );
};

export default WeekCell;
