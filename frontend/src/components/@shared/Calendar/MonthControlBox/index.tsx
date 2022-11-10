import { useContext } from 'react';
import { CalendarContext } from 'contexts/calendarContext';
import ArrowLeftIconSVG from 'assets/chevron-left.svg';
import ArrowRightIconSVG from 'assets/chevron-right.svg';
import * as S from './MonthControlBox.styled';

const MonthControlBox = () => {
  const { currentDate, navigateMonthTo, clearDateCellControlRef } =
    useContext(CalendarContext);

  return (
    <S.MonthControlBox>
      <S.ControlButton
        type="button"
        onClick={() => {
          clearDateCellControlRef();
          navigateMonthTo(-1);
        }}
      >
        <S.ChevronImage src={ArrowLeftIconSVG} alt="show previous month" />
      </S.ControlButton>
      <S.YearMonthSpan>
        {currentDate &&
          `${currentDate.getFullYear()}년 ${currentDate.getMonth() + 1}월`}
      </S.YearMonthSpan>
      <S.ControlButton
        type="button"
        onClick={() => {
          clearDateCellControlRef();
          navigateMonthTo(1);
        }}
      >
        <S.ChevronImage src={ArrowRightIconSVG} alt="show next month" />
      </S.ControlButton>
    </S.MonthControlBox>
  );
};

export default MonthControlBox;
