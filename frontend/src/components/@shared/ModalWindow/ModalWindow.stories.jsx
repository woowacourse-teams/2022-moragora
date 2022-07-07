import ModalWindow from '.';

export default {
  title: 'Components/ModalWindow',
  component: ModalWindow,
};

function Template(args) {
  return <ModalWindow {...args} />;
}

export const Default = Template.bind({});
