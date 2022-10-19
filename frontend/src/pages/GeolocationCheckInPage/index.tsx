import { useContext, useEffect, useRef, useState } from 'react';
import * as S from './GeolocationCheckInPage.styled';
import { userContext, UserContextValues } from 'contexts/userContext';
import { getMeetingListApi } from 'apis/meetingApis';
import Spinner from 'components/@shared/Spinner';
import ErrorIcon from 'components/@shared/ErrorIcon';
import ReloadButton from 'components/@shared/ReloadButton';
import CheckMeetingItem from 'components/CheckMeetingItem';
import useQuery from 'hooks/useQuery';
import { MeetingListResponseBody } from 'types/meetingType';
import emptyInboxSVG from 'assets/empty-inbox.svg';
import useGeolocation from 'hooks/useGeolocation';
import useKakaoMap from 'hooks/useKakaoMap';
import CheckInButtonSection from './CheckInButtonSection';

const GeolocationCheckInPage = () => {
  const [currentMeeting, setCurrentMeeting] = useState<
    MeetingListResponseBody['meetings'][0] | null
  >(null);
  const { accessToken } = useContext(userContext) as UserContextValues;
  const { currentPosition, isLoading } = useGeolocation();
  const { mapContainerRef, setControllable, panTo } = useKakaoMap();
  const mapOverlayRef = useRef<HTMLDivElement>(null);
  const mapUserMarkerRef = useRef<SVGSVGElement>(null);

  const meetingListQuery = useQuery(
    ['meetingList'],
    getMeetingListApi(accessToken),
    {
      onSuccess: ({ body: { meetings } }) => {
        const activeMeeting = meetings.find((meeting) => meeting.isActive);

        if (activeMeeting) {
          setCurrentMeeting(activeMeeting);
        }
      },
      onError: () => {
        alert('모임 정보를 가져오는데 실패했습니다.');
      },
    }
  );

  const handleMeetingItemClick = (
    meeting: MeetingListResponseBody['meetings'][0]
  ) => {
    setCurrentMeeting(meeting);
  };

  useEffect(() => {
    if (isLoading && mapOverlayRef.current && mapUserMarkerRef.current) {
      mapOverlayRef.current.classList.add('loading');
      mapUserMarkerRef.current.classList.add('loading');
    } else {
      mapOverlayRef.current?.classList.remove('loading');
      mapUserMarkerRef.current?.classList.remove('loading');

      if (currentPosition) {
        panTo(
          currentPosition.coords.latitude,
          currentPosition.coords.longitude
        );
      }
    }

    setControllable(false);
  }, [
    isLoading,
    mapOverlayRef.current,
    setControllable,
    panTo,
    currentPosition,
  ]);

  if (meetingListQuery.isLoading) {
    return (
      <S.Layout>
        <S.SpinnerBox>
          <Spinner />
        </S.SpinnerBox>
      </S.Layout>
    );
  }

  if (meetingListQuery.isError) {
    return (
      <S.Layout>
        <S.ErrorBox>
          <ErrorIcon />
          <ReloadButton
            onClick={() => {
              meetingListQuery.refetch();
            }}
          />
        </S.ErrorBox>
      </S.Layout>
    );
  }

  if (!currentMeeting) {
    return (
      <S.Layout>
        <S.EmptyStateBox>
          <S.EmptyStateImage src={emptyInboxSVG} alt="empty inbox" />
          <S.EmptyStateTitle>출석 가능한 모임이 없어요.</S.EmptyStateTitle>
        </S.EmptyStateBox>
      </S.Layout>
    );
  }

  return (
    <S.Layout>
      <S.MapSection>
        <S.Map ref={mapContainerRef}>
          <S.MapOverlay ref={mapOverlayRef} className="loading">
            Loading...
          </S.MapOverlay>
          <S.MapUserMarker
            ref={mapUserMarkerRef}
            width="2rem"
            height="2rem"
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 22"
            fill="currentColor"
            className="loading"
          >
            <path
              fillRule="evenodd"
              d="M11.54 22.351l.07.04.028.016a.76.76 0 00.723 0l.028-.015.071-.041a16.975 16.975 0 001.144-.742 19.58 19.58 0 002.683-2.282c1.944-1.99 3.963-4.98 3.963-8.827a8.25 8.25 0 00-16.5 0c0 3.846 2.02 6.837 3.963 8.827a19.58 19.58 0 002.682 2.282 16.975 16.975 0 001.145.742zM12 13.5a3 3 0 100-6 3 3 0 000 6z"
              clipRule="evenodd"
            />
          </S.MapUserMarker>
        </S.Map>
      </S.MapSection>
      <S.CheckTimeSection>
        <S.SectionTitle>출결 중인 모임</S.SectionTitle>
        <S.MeetingList>
          {meetingListQuery.data?.body.meetings
            .filter((meeting) => meeting.isActive)
            .map((meeting) => (
              <CheckMeetingItem
                key={meeting.id}
                onClick={handleMeetingItemClick}
                meeting={meeting}
                clicked={meeting.id === currentMeeting?.id}
              >
                {meeting.name}
              </CheckMeetingItem>
            ))}
        </S.MeetingList>
      </S.CheckTimeSection>
      <CheckInButtonSection
        meeting={currentMeeting}
        currentPosition={currentPosition}
        refetchMeetingList={meetingListQuery.refetch}
      />
    </S.Layout>
  );
};

export default GeolocationCheckInPage;
