import { useContext } from 'react';
import * as S from './MonthControlBox.styled';
import { CalendarContext } from 'contexts/calendarContext';
import ArrowLeftIconSVG from 'assets/chevron-left.svg';
import ArrowRightIconSVG from 'assets/chevron-right.svg';

const MonthControlBox = () => {
  const { navigateMonthTo, clearDateCellControlRef } =
    useContext(CalendarContext);

  return (
    <S.MonthControlBox>
      <button
        type="button"
        onClick={() => {
          clearDateCellControlRef();
          navigateMonthTo(-1);
        }}
      >
        <S.ChevronImage src={ArrowLeftIconSVG} alt="show previous month" />
      </button>
      <button
        type="button"
        onClick={() => {
          clearDateCellControlRef();
          navigateMonthTo(1);
        }}
      >
        <S.ChevronImage src={ArrowRightIconSVG} alt="show next month" />
      </button>
    </S.MonthControlBox>
  );
};

export default MonthControlBox;
