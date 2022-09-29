import { useContext, useState } from 'react';
import { ElementOf } from 'checkmate-ts-module';
import * as S from './MasterAssignSection.styled';
import Button from 'components/@shared/Button';
import ModalPortal from 'components/ModalPortal';
import ModalWindow from 'components/@shared/ModalWindow';
import { Participant } from 'types/userType';
import { useOutletContext } from 'react-router-dom';
import { QueryState } from 'types/queryType';
import { assignMasterApi, getMeetingData } from 'apis/meetingApis';
import { getUpcomingEventApi } from 'apis/eventApis';
import { MeetingResponseBody } from 'types/meetingType';
import request from 'utils/request';
import useMutation from 'hooks/useMutation';
import { userContext, UserContextValues } from 'contexts/userContext';

type SelectedParticipant = ElementOf<MeetingResponseBody['users']>;

const MasterAssignSection = () => {
  const { accessToken } = useContext(userContext) as UserContextValues;
  const [isDropdownOpened, setIsDropdownOpened] = useState(false);
  const [isModalOpened, setIsModalOpened] = useState(false);
  const { meetingQuery } = useOutletContext<{
    meetingQuery: QueryState<typeof getMeetingData>;
    upcomingEventQuery: QueryState<typeof getUpcomingEventApi>;
    totalTardyCount: number;
    upcomingEventNotExist: boolean;
  }>();
  const participants = meetingQuery.data?.body.users;
  const [selectedParticipant, setSelectedParticipant] =
    useState<SelectedParticipant>();

  const masterAssignMutation = useMutation(
    assignMasterApi(meetingQuery.data?.body.id as number, accessToken),
    {
      onSuccess: () => {
        meetingQuery.refetch();
        alert('마스터 권한을 넘겼습니다.');
      },
      onError: (e) => {
        alert(e.message);
      },
    }
  );

  const handleParticipantClick = (participant: SelectedParticipant) => () => {
    setIsDropdownOpened(false);
    setSelectedParticipant(participant);
    setIsModalOpened(true);
  };

  const handleMasterAssignModalConfirm = () => {
    if (selectedParticipant) {
      masterAssignMutation.mutate({ userId: selectedParticipant.id });
    }

    setIsModalOpened(false);
  };

  const handleMasterAssignModalDismiss = () => {
    setIsModalOpened(false);
  };

  return (
    <>
      {isModalOpened && (
        <ModalPortal closePortal={() => setIsModalOpened(false)}>
          <ModalWindow
            message={`${
              selectedParticipant?.nickname ?? '선택되지 않음'
            }에게 마스터를 넘길까요?`}
            onConfirm={handleMasterAssignModalConfirm}
            onDismiss={handleMasterAssignModalDismiss}
          />
        </ModalPortal>
      )}
      <S.Label>
        마스터 넘기기
        <S.MasterAssignBox>
          <S.SelectBox>
            <S.SelectSpan
              onClick={(e) => {
                setIsDropdownOpened((prev) => !prev);
              }}
            >
              {selectedParticipant?.nickname ?? '선택되지 않음'}
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="1rem"
                height="1rem"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
                strokeWidth={2}
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M19 9l-7 7-7-7"
                />
              </svg>
            </S.SelectSpan>
            에게 마스터 넘기기
          </S.SelectBox>
          {isDropdownOpened && (
            <S.ParticipantList>
              {participants
                ?.filter(({ isMaster }) => !isMaster)
                .map((participant) => (
                  <S.ParticipantListItem
                    key={participant.id}
                    onMouseDown={(e) => {
                      e.preventDefault();
                    }}
                    onClick={handleParticipantClick(participant)}
                  >
                    {participant.nickname}
                  </S.ParticipantListItem>
                ))}
            </S.ParticipantList>
          )}
        </S.MasterAssignBox>
      </S.Label>
    </>
  );
};

export default MasterAssignSection;
