import ModalWindow from '.';

export default {
  title: 'Components/ModalWindow',
  component: ModalWindow,
};

function Template(args) {
  return <ModalWindow {...args} />;
}

export const Default = Template.bind({});
Default.args = {
  message: '정말 삭제하시겠습니까?',
  onConfirm: () => {
    alert('삭제했습니다.');
  },
  onDismiss: () => {
    alert('취소했습니다.');
  },
};
