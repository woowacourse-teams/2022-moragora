import { CalendarContext } from 'contexts/calendarContext';
import { userContext, UserContextValues } from 'contexts/userContext';
import { useContext } from 'react';
import { Navigate, useNavigate, useParams } from 'react-router-dom';
import * as S from './CalendarPage.styled';

const CalendarPage = () => {
  const { id: meetingId } = useParams();

  if (!meetingId) {
    return <Navigate to="/error" />;
  }

  const user = useContext(userContext) as UserContextValues;
  const calander = useContext(CalendarContext);

  return <S.Layout></S.Layout>;
};

export default CalendarPage;
