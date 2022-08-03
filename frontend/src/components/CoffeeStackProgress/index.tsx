import React from 'react';
import * as S from './CoffeeStackProgress.styled';
import CircleProgressBar from 'components/@shared/CircleProgressBar';
import coffeePotIcon from 'assets/coffee-pot.svg';
import coffeeCupIcon from 'assets/coffee-cup.svg';

type CoffeeStackProgressProps = {
  percent: number;
};

const CoffeeStackProgress: React.FC<
  React.PropsWithChildren<CoffeeStackProgressProps>
> = ({ percent }) => {
  const size = 200;

  return (
    <CircleProgressBar size={size} percent={percent}>
      <S.CoffeeIcon
        size={size / 2}
        src={percent >= 100 ? coffeeCupIcon : coffeePotIcon}
        alt="coffee"
      />
    </CircleProgressBar>
  );
};

export default CoffeeStackProgress;
