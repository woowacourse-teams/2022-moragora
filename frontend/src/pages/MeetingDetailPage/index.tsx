import { useContext, useEffect, useRef, useState } from 'react';
import { Navigate, Outlet, useParams } from 'react-router-dom';
import useQuery from 'hooks/useQuery';
import { getUpcomingEventApi } from 'apis/eventApis';
import { getMeetingData } from 'apis/meetingApis';
import ReloadButton from 'components/@shared/ReloadButton';
import ErrorIcon from 'components/@shared/ErrorIcon';
import Spinner from 'components/@shared/Spinner';
import { CalendarContext } from 'contexts/calendarContext';
import * as S from './MeetingDetailPage.styled';

const MeetingDetailPage = () => {
  const { id } = useParams();

  if (!id) {
    return <Navigate to="/error" />;
  }

  const [totalTardyCount, setTotalTardyCount] = useState(0);
  const [upcomingEventNotExist, setUpcomingEventNotExist] = useState(false);
  const { clearSelectedDates } = useContext(CalendarContext);

  const meetingQuery = useQuery(['meeting'], getMeetingData(id), {
    onSuccess: ({ data: { users } }) => {
      setTotalTardyCount(
        users.reduce((total, user) => total + user.tardyCount, 0)
      );
    },
  });

  const upcomingEventQuery = useQuery(
    ['upcomingEvent'],
    getUpcomingEventApi(id),
    {
      enabled: meetingQuery.isSuccess,
      onError: (error) => {
        setUpcomingEventNotExist(
          parseInt(error.message.split(': ')[0], 10) === 404
        );
      },
    }
  );

  const tabsRef = useRef<Record<string, HTMLElement | undefined>>({});
  const bindTabRef =
    (key: string): React.RefCallback<HTMLAnchorElement> =>
    (element) => {
      if (!element) {
        return;
      }

      tabsRef.current[key] = element;
    };

  const tabPositionMap = () => {
    const positionMap: Record<
      string,
      ReturnType<HTMLElement['getBoundingClientRect']>
    > = {};

    Object.entries(tabsRef.current).forEach(([key, element]) => {
      if (element) {
        positionMap[key] = element.getBoundingClientRect();
      }
    });

    return Object.values(positionMap);
  };

  useEffect(() => {
    clearSelectedDates();
  }, [id]);

  if (meetingQuery.isLoading || upcomingEventQuery.isLoading) {
    return (
      <S.Layout>
        <S.SpinnerBox>
          <Spinner />
        </S.SpinnerBox>
      </S.Layout>
    );
  }

  if (!id || meetingQuery.isError) {
    return (
      <S.Layout>
        <S.TabNavBox>
          <S.TabNav>
            <S.TabNavLink to="coffee-stack" ref={bindTabRef('coffee-stack')}>
              커피스택
            </S.TabNavLink>
            <S.TabNavLink to="calendar" ref={bindTabRef('calendar')}>
              일정
            </S.TabNavLink>
            <S.TabNavLink to="config" ref={bindTabRef('config')}>
              설정
            </S.TabNavLink>
            <S.IndicatorBox tabPositions={tabPositionMap()}>
              <svg
                width="18"
                height="5"
                viewBox="0 0 18 5"
                fill="currentColor"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  d="M3.06725 0.366895C6.97724 -0.122283 10.71 -0.122314 14.7987 0.366895C18.8875 0.856104 19.243 2.69073 14.7987 4.03621C10.3545 5.38169 7.33274 5.25938 3.06725 4.03621C-1.19824 2.81304 -0.842737 0.856074 3.06725 0.366895Z"
                  fill="inherit"
                />
              </svg>
            </S.IndicatorBox>
          </S.TabNav>
        </S.TabNavBox>
        <S.ErrorBox>
          <ErrorIcon />
          <ReloadButton
            onClick={() => {
              meetingQuery.refetch();
            }}
          />
        </S.ErrorBox>
      </S.Layout>
    );
  }

  return (
    <S.Layout>
      <S.TabNavBox>
        <S.TabNav>
          <S.TabNavLink to="coffee-stack" ref={bindTabRef('coffee-stack')}>
            커피스택
          </S.TabNavLink>
          <S.TabNavLink to="calendar" ref={bindTabRef('calendar')}>
            일정
          </S.TabNavLink>
          <S.TabNavLink to="config" ref={bindTabRef('config')}>
            설정
          </S.TabNavLink>
          <S.IndicatorBox tabPositions={tabPositionMap()}>
            <svg
              width="18"
              height="5"
              viewBox="0 0 18 5"
              fill="currentColor"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M3.06725 0.366895C6.97724 -0.122283 10.71 -0.122314 14.7987 0.366895C18.8875 0.856104 19.243 2.69073 14.7987 4.03621C10.3545 5.38169 7.33274 5.25938 3.06725 4.03621C-1.19824 2.81304 -0.842737 0.856074 3.06725 0.366895Z"
                fill="inherit"
              />
            </svg>
          </S.IndicatorBox>
        </S.TabNav>
      </S.TabNavBox>
      <S.MainBox>
        <Outlet
          context={{
            meetingQuery,
            upcomingEventQuery,
            totalTardyCount,
            upcomingEventNotExist,
          }}
        />
      </S.MainBox>
    </S.Layout>
  );
};

export default MeetingDetailPage;
