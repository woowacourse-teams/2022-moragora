import React from 'react';
import CoffeeIconSVG from 'assets/coffee.svg';
import CrownIconSVG from 'assets/crown.svg';
import { Participant } from 'types/userType';
import { Meeting } from 'types/meetingType';
import * as S from './CoffeeStackItem.styled';

type CoffeeStackItemProps = {
  name: Participant['nickname'] | Meeting['name'];
  tardyCount: Participant['tardyCount'];
  isMaster?: boolean;
};

const CoffeeStackItem: React.FC<CoffeeStackItemProps> = ({
  name,
  tardyCount,
  isMaster,
}) => {
  return (
    <S.Layout aria-label={`${name} 커피스택 ${tardyCount}개`}>
      <S.NameBox>
        <span>{name}</span>
        {isMaster && <img src={CrownIconSVG} alt="crown-icon" width={20} />}
      </S.NameBox>
      <S.CoffeeStackBox>
        {Array.from({ length: tardyCount }).map((_, index) => (
          <S.CoffeeIconImage
            key={index}
            src={CoffeeIconSVG}
            aria-hidden="true"
          />
        ))}
      </S.CoffeeStackBox>
    </S.Layout>
  );
};

export default CoffeeStackItem;
