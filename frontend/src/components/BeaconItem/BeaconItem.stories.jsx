import BeaconItem from '.';

export default {
  title: 'Components/BeaconItem',
  component: BeaconItem,
};

const Template = (args) => {
  return <BeaconItem {...args} />;
};

export const Default = Template.bind({});
Default.args = {
  id: 1,
  position: {
    Ma: 27.123120398102938,
    La: 27.129481204127949,
  },
  address: {
    address_name: '서울 송파구 신천동 7-28',
  },
  radius: 50,
  panTo: () => {},
  remove: () => {},
};
