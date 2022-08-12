import { useRef } from 'react';
import { Outlet } from 'react-router-dom';
import * as S from './MeetingDetailPage.styled';

const MeetingDetailPage = () => {
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

  return (
    <S.Layout>
      <S.TabNav>
        <S.Tab to={`coffee-stack`} ref={bindTabRef(`coffee-stack`)}>
          커피스택
        </S.Tab>
        <S.Tab to={`event-create`} ref={bindTabRef(`event-create`)}>
          일정
        </S.Tab>
        <S.Tab to={`config`} ref={bindTabRef(`config`)}>
          설정
        </S.Tab>
        <S.Indicator tabPositions={tabPositionMap()}>
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
        </S.Indicator>
      </S.TabNav>
      <S.Main>
        <Outlet />
      </S.Main>
    </S.Layout>
  );
};

export default MeetingDetailPage;
