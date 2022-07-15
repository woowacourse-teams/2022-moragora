import * as S from './InputHint.styled';

type InputHintProps = {
  condition: boolean;
  message: string;
};

const InputHint: React.FC<InputHintProps> = ({ condition, message }) => {
  return condition && <S.Paragraph>{message}</S.Paragraph>;
};

export default InputHint;
