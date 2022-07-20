import React from 'react';
import * as S from './CoffeeStackItem.styled';
import CoffeeIconSVG from '../../assets/coffee.svg';
import { MeetingItem } from 'types/meetingType';

type CoffeeStackItemProps = { meeting: MeetingItem };

const CoffeeStackItem: React.FC<CoffeeStackItemProps> = ({
  meeting: { name },
}) => {
  return (
    <S.Layout>
      <span>{name}</span>
      <S.Box>
        <S.CoffeeIconImage src={CoffeeIconSVG} />
        <S.CoffeeIconImage src={CoffeeIconSVG} />
        <S.CoffeeIconImage src={CoffeeIconSVG} />
      </S.Box>
    </S.Layout>
  );
};

export default CoffeeStackItem;
